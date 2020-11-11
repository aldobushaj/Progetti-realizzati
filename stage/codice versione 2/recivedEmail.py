#!/usr/bin/python3
import boto3
import smtplib
import time
import imaplib
import email
from datetime import datetime
import os
import re
# -------------------------------------------------
#
# Class wich read email from Gmail Using Python and access to databases in AWS
#
# ------------------------------------------------
FROM_EMAIL  = "spotifyba.97@gmail.com"
FROM_PWD    = "Ciao.123"
SMTP_SERVER = "imap.gmail.com"
SMTP_PORT   = 993
BUCKET = "images-sender"
COLLECTION = "collection"
dynamodb = boto3.client('dynamodb')
rekognition = boto3.client("rekognition",'eu-central-1')
s3 = boto3.resource('s3')

# inserisce in s3 la foto(con la corrispindente chiave "nome.jpg") con il suo nome e cognome
def insert_in_s3(filename,full_name):
    path = "attachment/" + filename
    # apro l'immagine scaricata precedentemente nell'allegato
    file = open(path,'rb')
    # e lo inserisco in s3
    ret = s3.Bucket("images-sender").put_object(Key=filename, Body=file, Metadata={'FullName':full_name})


# inserisce in dynamoDb l'id del volto con il suo nome e cognome
def insert_in_dynamoDb(faceId,full_name):
    # inserisco l'id del volto corrente in dynamoDb
    response = dynamodb.put_item(
    TableName='sender_id',
    Item={
        'RekognitionId': {'S': faceId},
        'FullName': {'S': full_name}
        }
    )
# inserisce nella collection indicata il volto specificato e chiama il metodo update_index
def index_faces(key,full_name,attributes=()):
    response = rekognition.index_faces(
        CollectionId=COLLECTION,
        Image={
            "S3Object": {
                "Bucket": BUCKET,
                "Name": key
            }
        },
        ExternalImageId=key,
        DetectionAttributes=attributes,
    )
    if response['ResponseMetadata']['HTTPStatusCode'] == 200:
        faceId = response['FaceRecords'][0]['Face']['FaceId']
        print("Uploaded in collection : "+full_name)
        insert_in_dynamoDb(faceId,full_name)
    else :
        return False
        
    return True

#Restituisce il plain text dall'email .
def get_text(msg, fallback_encoding='utf-8', errors='replace'):
    text = ""
    for part in msg.walk():
        # verifico che il messaggio passato come parametro si di tipo 'text/plain'
        if part.get_content_type() == 'text/plain':
            # a questo punto posso decodificare il messaggio (ad esempio al posto degli a capo in 'p' ci sarà "\n" e cosi via) ottenuto da get_payload
            p = part.get_payload(decode=True)
            if p is not None:
                # inserisco nella variabile text il contenuto della variabile p codificata in utf-8
                text = p.decode(fallback_encoding,errors)
                # alternativa con text = [] 
                #text.append(p.decode(part.get_charset() or fallback_encoding,errors)) 
                
    # quindi restituisco semplicemente text
    return text
    # alternativa 
    # return "\n".join(text)

def save_attachment(msg, download_folder="/home/pi/Desktop/attachment"):
    """
    Dato un messaggio, salva l'allegato in una cartella specifica (default è /tmp)

    return: filename del file allegato
    """
    att_path = "No attachment found."
    for part in msg.walk():      
        # se part e di tipo multipart allora riinizio il ciclo
        if part.get_content_type() == 'multipart':
            continue
        
        # se in part 'Content-Disposition' è None riinizio il ciclo 
        if part.get('Content-Disposition') is None:
            continue
        
        # recupero il nome del file in allegato
        filename = part.get_filename()
        # e lo unisco al percorso specificato(la cartella dove verrà scaricato)
        att_path = os.path.join(download_folder, filename)
        
        # verifico che il percorso sia corretto, quindi salvo il file nel percorso specificato
        if not os.path.isfile(att_path):
            fp = open(att_path, 'wb')
            fp.write(part.get_payload(decode=True))
            fp.close()
    # restituisco il percorso in cui è stato salvato il file se c'è un allegato,altrimenti notifico la mancanza di allegati nell'email specificata
    return filename

# la funzione ritorna true se "key" è presente in "string" grazie all'utilizzo di f il quale formatta la stringa
def contains_word(string , key):
    return f' {key} ' in f' {string} '

# funzione con la quale è possibile ottenere varie informazioni sull'email(mittente, data, corpo del messaggio , ecc.)
def read_email_from_gmail():
    try:
        current_time = datetime.now()
        
        mail = imaplib.IMAP4_SSL(SMTP_SERVER)
        mail.login(FROM_EMAIL,FROM_PWD)
        mail.select('inbox')
        
        # cerco tutte e solo le email ricevute dal nome utente specificato(seguito dal dominio @gmail.com), con corpo contenente la stringa specificata,
        # aggiungendo prima di None 'UNSEEN',  seleziono tutte le email non ancora lette dall'email specificata con corpo contenente la stringa specificata
        type, data = mail.search(None,'FROM "bushajaldo@gmail.com" BODY "Forwarded message"') #'ALL')     oppure    SUBJECT "ADD" ecc.
        
        mail_ids = data[0]
        id_list = mail_ids.split() 
        first_email_id = int(id_list[0])
        #print("first_email_id => ",first_email_id)  
        latest_email_id = int(id_list[-1])
        #print("latest_email_id => ",latest_email_id)  
        
        # contiene ad ogni posizione diverse informazioni sull'email
        # variabile di tipo dictionary che contiene tutte le informazioni di ciascuna email(from,data,body)
        emails_info= {} 
        #numero per iterare nel dictionary
        num = latest_email_id - first_email_id
        
        #parto dall'ultimo messaggio al primo-1(per leggere anche l'ulitmo), specificando con il 3 parametro di sottrare 1 ad ogni ciclo
        for i in range(latest_email_id,first_email_id-1, -1):
            typ, data = mail.fetch(str(i), '(RFC822)' )
            for response_part in data:
                if isinstance(response_part, tuple):
                    msg = email.message_from_bytes(response_part[1])
                    email_subject = msg['subject']
                    email_from = msg['from']
                    email_date = msg['date']
                    
                    email_dict = {
                        "Date": email_date,
                        "From": email_from,
                        "Subject": email_subject,
                        "Body" : get_text(msg),
                        "Filename": save_attachment(msg)
                    }
                    #alla prima posizione(num) informazioni relative alla prima email, alla seconda posizione informazioni relative alla seconda email e cosi via
                    emails_info[num]= email_dict
            #incremento num per inserire alla prossima posizione informazioni sull' email seguente     
            num=num-1
                   
        # per tutte le email contenute nella variabile email_info
        for pos in range(len(emails_info)):
            # se il corpo dell'email inoltrata contiene la parola chiave(nel nostro caso ADD), significa che l'utente vuole aggiungere il mittente al database,quindi
            if contains_word(emails_info[pos]['Body'],"ADD"):
                # seleziono tutte le stringhe che inziano con una lettera dell'alfabeto maiuscola seguita da almeno una lettera minuscola(fino a n) seguita da 
                # un solo spazio bianco, tutto questo per 2 o 3 volte(3 volte per includere i secondi nomi)
                # quindi name conterrà il nome(eventualmente anche il secondo nome) e cognome del mittente nell'email corrente
                # .strip elimina gli spazi all'inizio e alla fine della stringa, in modo da ottenere solo il nome completo del volto
                full_name = (re.search(r"([A-Z]{1}[a-z]+\s{1}){2,3}",str(emails_info[pos]['Body'])).group()).strip()
                # la chiave della foto sarà data dal nome indicato, seguito dal formato .jpg
                file_name = (full_name.split(" ")[0]).lower() + ".jpg"
                insert_in_s3(file_name,full_name)
                if index_faces(file_name,full_name):
                    print("Image successfully uploaded in collection and dynamoDb)
                    # quindi le metto nella cartella delle immagini gia caricate nei database
                    os.replace("attachment/"+file_name, "attachment/uploaded/"+file_name)
                else: 
                    print("ERRORR.... Image not uploaded")
                         
        #effettuo il logout dal server  
        mail.logout()
    except Exception as e:
        print(str(e))
read_email_from_gmail()