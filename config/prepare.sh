#!/bin/bash

function usage()
{
    echo "Usage: prepare.sh <app id>"
    echo " where app id can currently be one of [1 2]"
}

if [ -z "$1" ]
then
    usage
    exit 1
fi

case "$1" in
    1)  echo 1
        cp config/1/AndroidManifest.xml .
        cp -R config/1/res .
        ;;
    2)  echo 2
        cp config/2/AndroidManifest.xml .
        cp -R config/2/res .
        ;;
    *)  usage
        ;;
esac

