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

function removeOldSourceFilesForOtherPackage()
{
    packageName=$1
    rm -r src/main/java/com/chewielouie/$packageName
    rm -r src/test/java/com/chewielouie/$packageName
}

function copyCommonFiles()
{
    cp -R config/common/res .
}

function copyTestFilesForPackage()
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

function copyFilesFromConfigArea()
{
    configArea=$1
    cp config/$configArea/AndroidManifest.xml .
    cp -R config/$configArea/res .
    cp -R config/$configArea/src .
}

if [ -z "$1" ]
then
    usage
    exit 1
fi

case "$1" in
    1)  echo "Configuring app id 1"
        removeOldFiles
        removeOldSourceFilesForOtherPackage textadventure2
        copyCommonFiles
        copyTestFilesForPackage textadventure
        copyFilesFromConfigArea 1
        ;;
    2)  echo "Configuring app id 2"
        removeOldFiles
        removeOldSourceFilesForOtherPackage textadventure
        copyCommonFiles
        copyTestFilesForPackage textadventure2
        copyFilesFromConfigArea 2
        ;;
    *)  usage
        ;;
esac

