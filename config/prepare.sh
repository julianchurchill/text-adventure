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

function copyCommonFiles()
{
    cp -R config/common/res .
}

if [ -z "$1" ]
then
    usage
    exit 1
fi

case "$1" in
    1)  echo "Configuring app id 1"
        removeOldFiles
        rm -r src/main/java/com/chewielouie/textadventure2
        copyCommonFiles
        cp config/1/AndroidManifest.xml .
        cp -R config/1/res .
        cp -R config/1/src .
        ;;
    2)  echo "Configuring app id 2"
        removeOldFiles
        rm -r src/main/java/com/chewielouie/textadventure
        copyCommonFiles
        cp config/2/AndroidManifest.xml .
        cp -R config/2/res .
        cp -R config/2/src .
        ;;
    *)  usage
        ;;
esac

