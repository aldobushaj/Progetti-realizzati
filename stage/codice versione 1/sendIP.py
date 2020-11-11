#!/usr/bin/python
def sendEmail(IPAddr): 
    import smtplib 
    # creates SMTP session 
    s = smtplib.SMTP('smtp.gmail.com', 587) 

    # start TLS for security 
    s.starttls() 

    # Authentication 
    s.login("spotifyBA.97@gmail.com", "Ciao.123") 

    SUBJECT = "You have new IP"

    # message to be sent 
    TEXT = "The new IP is  "+IPAddr
    # add the subject to the object "email"
    message = 'Subject: {}\n\n{}'.format(SUBJECT, TEXT)

    # sending the mail 
    s.sendmail("spotifyBA.97@gmail.com", "bushajaldo@gmail.com", message) 

    # terminating the session 
    s.quit() 
    
import socket    
hostname = socket.gethostname()              #store the hostname in a variable 
IPAddr = socket.gethostbyname(hostname)     # store in a variable the IP address of "hostname"

f = open("/home/pi/Desktop/ip.txt", "r")
ipSaved = f.readline()
# if the ip saved in a file "ip.txt" isn't equals to Ip addrress stored in a variable, modify the file and send an email
if ipSaved != IPAddr:
    f = open("/home/pi/Desktop/ip.txt","w")
    f.write(IPAddr)
    sendEmail(IPAddr)
