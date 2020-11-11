#!/usr/bin/python
# coding=utf-8
import boto3
from decimal import Decimal
import json
import urllib

BUCKET = "image-sender"
#IMAGE_ID = KEY  # S3 key as ImageId
COLLECTION = "collection"
dynamodb = boto3.client('dynamodb', "eu-central-1")
s3 = boto3.client('s3')

# variabile che conterrà tutte le chiavi
all_key = [] 
# funzione che recupera le chiavi di tutti gli oggetti in S3 e li mette nella variabile "all_key"
def get_all_key(bucket, suffix=''):
    kwargs = {'Bucket': bucket}

    while True:

        # The S3 API response is a large blob of metadata.
        # 'Contents' contains information about the listed objects.
        resp = s3.list_objects_v2(**kwargs)
        for obj in resp['Contents']:
            key = obj['Key']
            if key.endswith(suffix):
                yield key

        # The S3 API is paginated, returning up to 1000 keys at a time.
        # Pass the continuation token into the next response, until we
        # reach the final page (when this field is missing).
        try:
            kwargs['ContinuationToken'] = resp['NextContinuationToken']
        except KeyError:
            break
# inserisco tutte le chiavi degli oggetti con suffisso .jpg in "all_key"
for key in (get_all_key(BUCKET,'.jpg')):
    all_key.append(key)
# Note: you have to create the collection first!
# rekognition.create_collection(CollectionId=COLLECTION)


    #print(key)

       
def update_index(tableName,faceId, fullName):
	response = dynamodb.put_item(
	TableName=tableName,
	Item={
		'RekognitionId': {'S': faceId},
		'FullName': {'S': fullName}
		}
	)
	#print(response)
def index_faces(bucket, key, collection_id, attributes=(), region="eu-central-1"):
    status = True # indica se tutte le immagini sono state caricate correttamente su dynamodb
    for index in range(0,len(key)):
		rekognition = boto3.client("rekognition", region)
		response = rekognition.index_faces(
			Image={
				"S3Object": {
					"Bucket": bucket,
					"Name": key[index],
				}
			},
			CollectionId=collection_id,
			ExternalImageId=all_key[index],
			DetectionAttributes=attributes,
		)
		if response['ResponseMetadata']['HTTPStatusCode'] == 200:
			faceId = response['FaceRecords'][0]['Face']['FaceId']
			#print(faceId)
			ret = s3.head_object(Bucket=bucket,Key=key[index])
			personFullName = ret['Metadata']['fullname']
			#print(ret)
			print(personFullName)
			update_index('sender_collection',faceId,personFullName)
		else:
			status = False
		# Print response to console.
		#print("Ecco cosa ritorna "+str(response['FaceRecords'])+ " \ne response "+str(response))
    #return response['FaceRecords']
    return status

if index_faces(BUCKET, all_key, COLLECTION):
	print("Images succesfully uploaded")
else: 
    print("ERRORR.... Images not uploaded")

'''for record in:
    face = record['Face']
	# details = record['FaceDetail']
    print "Face ({}%)".format(face['Confidence'])
    print "  FaceId: {}".format(face['FaceId'])
    print "  ImageId: {}".format(face['ImageId'])'''
