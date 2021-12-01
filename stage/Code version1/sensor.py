#!/usr/bin/python3
import time
# Librerie per inviare l'email
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.base import MIMEBase
from email import encoders
# Librerie per la fotocamera
from picamera import PiCamera
from time import sleep
import smtplib
# Librerie per il sensore di prossimità
import board
import busio
import adafruit_vcnl4040
import boto3
s3 = boto3.resource('s3')


# Metodo che invia l'email con la foto in allegato quando viene ricevuta la posta
def send_email():
    fromaddr = "spotifyBA.97@gmail.com"
    toaddr = "bushajaldo@gmail.com"

    # Istanza di MIMEMultipart
    msg = MIMEMultipart()

    # Memorizzo l'email del mittente
    msg['From'] = fromaddr

    # Memorizzo l'email del destinatario
    msg['To'] = toaddr

    # Memorizzo l'ggetto
    msg['Subject'] = "HAI RICEVUTO UNA LETTERA!!"

    # Stringa che verra inviata nel corpo dell'email
    body = "Hai ricevuto una nuova lettera, in allegato la foto del mittente."

    # Collega il corpo con l'istanza di msg
    msg.attach(MIMEText(body, 'plain'))

    # Apre il file da inviare
    filename = "image.jpg"
    attachment = open("/home/pi/Desktop/"+filename, "rb")

    # Istanza di MIMEBase e memorizzata nella varibile p
    p = MIMEBase('application', 'octet-stream')

    # Per modificare il payload in forma codificata
    p.set_payload((attachment).read())

    # Codifico in base64
    encoders.encode_base64(p)

    p.add_header('Content-Disposition', "attachment; filename= %s" % filename)

    # Collego l'istanza 'p' all'istanza 'msg'
    msg.attach(p)

    # Creo la sessione SMTP
    s = smtplib.SMTP('smtp.gmail.com', 587)

    # Avvia TLS per crittografare le informazioni trasmesse mediante tale protocollo
    s.starttls()

    # Autenticazione
    s.login(fromaddr, "Ciao.123")

    # Converte Multipart msg in una stringa
    text = msg.as_string()

    # Manda la mail
    s.sendmail(fromaddr, toaddr, text)

    # Termina la sessione
    s.quit()


# Carica la foto nell' S3 bucket
def upload_image():
    file = open('image.jpg', 'rb')
    object = s3.Object('image-sender', 'image.jpg')
    ret = object.put(Body=file,
                     Metadata={'FullName': 'Guest'}
                     )
    print(ret)
    return


# Metodo che scatta la foto e la memorizza nel percorso specificato
def capture_image():
    camera = PiCamera()
    camera.start_preview()
    # attendo 5 secondi per dare il tempo ai sensori di luce della fotocamera di stabilizzarsi, dopodichè scatto la foto con il prossimo comando
    sleep(5)
    camera.capture('/home/pi/Desktop/image.jpg')
    camera.stop_preview()
    camera.close()
    return


# Codice sensore di prossimità
# connettori collegati ai pin SDA - SCL per il protocollo I2C
i2c = busio.I2C(board.SCL, board.SDA)
sensor = adafruit_vcnl4040.VCNL4040(i2c)

while sensor.proximity <= 5:
    # se non viene rilevato qualcosa a una distanza di "5" fa semplicemente una stampa e ricontrolla nuovamente dopo 0.5 secondi
    print("No mail recived")
    time.sleep(0.5)
# è stato rilevato a una distanza > 5 quindi faccio la foto (e mando l'email )
print("Proximity:", sensor.proximity, "then take a photo")
capture_image()
upload_image()
send_email()
#print("Light:", sensor.light)
#print("White:", sensor.white)
