#!/usr/bin/python        
# Method that checks if the ip has changed and sen email if changed
def sendEmail(IPAddr): 
    # libraries to be imported 
    import smtplib 
    from email.mime.multipart import MIMEMultipart 
    from email.mime.text import MIMEText 
    from email.mime.base import MIMEBase 
    from email import encoders 

    fromaddr = "spotifyBA.97@gmail.com"
    toaddr = "bushajaldo@gmail.com"

    # instance of MIMEMultipart 
    msg = MIMEMultipart() 

    # storing the senders email address 
    msg['From'] = fromaddr 

    # storing the receivers email address 
    msg['To'] = toaddr 

    # storing the subject 
    msg['Subject'] = "You have new IP!!!"

    # string to store the body of the mail 
    body = "The new IP is  "+IPAddr
    
    # attach the body with the msg instance 
    msg.attach(MIMEText(body, 'plain')) 
    # if i want to send an attached file uncomment this lines
    '''
    # open the file to be sent 
    filename = "ip.txt"
    attachment = open("/home/pi/Desktop/ip.txt", "rb") 

    # instance of MIMEBase and named as p 
    p = MIMEBase('application', 'octet-stream') 

    # To change the payload into encoded form 
    p.set_payload((attachment).read()) 

    # encode into base64 
    encoders.encode_base64(p) 

    p.add_header('Content-Disposition', "attachment; filename= %s" % filename) 

    # attach the instance 'p' to instance 'msg' 
    msg.attach(p) 
    '''
    # creates SMTP session 
    s = smtplib.SMTP('smtp.gmail.com', 587) 

    # start TLS for security 
    s.starttls() 

    # Authentication 
    s.login(fromaddr, "Ciao.123") 

    # Converts the Multipart msg into a string 
    text = msg.as_string() 

    # sending the mail 
    s.sendmail(fromaddr, toaddr, text) 

    # terminating the session 
    s.quit() 

import socket    
hostname = socket.gethostname()              #store the hostname in a variable 
IPAddr = socket.gethostbyname(hostname)     # store in a variable the IP address of "hostname"

f = open("ip.txt", "r")
ipSaved = f.readline()
# if the ip saved in a file "ip.txt" isn't equals to Ip addrress stored in a variable, modify the file and send an email
if ipSaved != IPAddr:
    f = open("ip.txt","w")
    f.write(IPAddr)
    sendEmail(IPAddr)