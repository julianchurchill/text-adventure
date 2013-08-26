#!/bin/bash

function usage()
{
    echo "Usage: prepare.sh <app id>"
    echo " where app id can currently be one of [1 2]"
}

function removeOldFiles()
{
    rm -r AndroidManifest.xml res
}

if [ -z "$1" ]
then
    usage
    exit 1
fi

case "$1" in
    1)  echo "Configuring app id 1"
        removeOldFiles
        cp config/1/AndroidManifest.xml .
        cp -R config/1/res .
        ;;
    2)  echo "Configuring app id 2"
        removeOldFiles
        cp config/2/AndroidManifest.xml .
        cp -R config/2/res .
        ;;
    *)  usage
        ;;
esac

