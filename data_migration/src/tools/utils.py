__author__='sherwin.she'
#
import uuid
import datetime


def getNewID():
	return str(uuid.uuid1()).replace('-','')
	pass

def now():
	return datetime.datetime.now()
	pass