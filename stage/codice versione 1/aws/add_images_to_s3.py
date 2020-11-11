#!/usr/bin/python
import boto3

s3 = boto3.resource('s3')

# Get list of objects for indexing
images=[('aldo.jpg','Aldo Bushaj'),
      ('nino.jpg','Antonino Bushaj'),
      ('silvestro.jpg','Silvestro Frisullo'),
      ('vlad.jpg','Vlad Axinte'),
      ('florian.jpg','Florian Karici'),
      ('supermario.jpg','Super Mario')
      ]

# Iterate through list to upload objects to S3   
for image in images:
      file = open(image[0],'rb')
      ret = s3.Bucket("image-sender").put_object(Key=image[0], Body=file, Metadata={'FullName':image[1]})
      
      '''object = s3.Object('image-sender', image[0])
      ret = object.put(Body=file,
                        Metadata={'FullName':image[1]}
                       )'''
      print(ret)