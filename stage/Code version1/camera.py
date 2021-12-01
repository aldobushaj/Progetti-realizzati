#!/usr/bin/python3
from picamera import PiCamera
from time import sleep

camera = PiCamera()
camera.start_preview()
#attendo 5 secondi per dare il tempo ai sensori di luce della fotocamera di stabilizzarsi, dopodichè scatto la foto con il prossimo comando
sleep(5)
camera.capture('/home/pi/Desktop/image.jpg')
camera.stop_preview()

# Take a video
'''
camera.start_preview()
camera.start_recording('/home/pi/Desktop/video.h264')
sleep(10)
camera.stop_recording()
camera.stop_preview()
'''