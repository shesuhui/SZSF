__author__='sherwin.she'
#
import uuid


def getNewID():
	return str(uuid.uuid1()).replace('-','')
	pass