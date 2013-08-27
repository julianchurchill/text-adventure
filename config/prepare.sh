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

function copyTestFiles()
{
    packageName=$1
    mkdir -p src/test/java/com/chewielouie/$packageName
    cp config/common/test/* src/test/java/com/chewielouie/$packageName
    monkeyPatchPackageNamesForTestFiles $packageName
}

function monkeyPatchPackageNamesForTestFiles()
{
    packageName=$1
    for file in src/test/java/com/chewielouie/$packageName/* ; do
        sed -e "s/package REPLACE_ME/package com.chewielouie.$packageName/" $file > $file.tmp && mv $file.tmp $file
    done
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
        rm -r src/test/java/com/chewielouie/textadventure2
        copyCommonFiles
        copyTestFiles textadventure
        cp config/1/AndroidManifest.xml .
        cp -R config/1/res .
        cp -R config/1/src .
        ;;
    2)  echo "Configuring app id 2"
        removeOldFiles
        rm -r src/main/java/com/chewielouie/textadventure
        rm -r src/test/java/com/chewielouie/textadventure
        copyCommonFiles
        copyTestFiles textadventure2
        cp config/2/AndroidManifest.xml .
        cp -R config/2/res .
        cp -R config/2/src .
        ;;
    *)  usage
        ;;
esac

