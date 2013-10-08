#!/bin/bash

function usage()
{
    echo "Usage: prepare.sh <app id>"
    echo " where app id can currently be one of [1 2]"
}

function removeOldFiles()
{
    rm --recursive AndroidManifest.xml res
}

function removeOldSourceFilesForOtherPackage()
{
    packageName=$1
    rm --recursive src/main/java/com/chewielouie/$packageName
    rm --recursive src/test/java/com/chewielouie/$packageName
}

function copy()
{
    src=$1
    dest=$2
    cp --update --recursive $src $dest
}

function copyCommonFiles()
{
    copy config/common/res .
}

function copyTestFilesForPackage()
{
    packageName=$1
    testDirectoryRoot=src/test/java/com/chewielouie
    testDirectory=$testDirectoryRoot/$packageName
    mkdir --parents $testDirectory
    copy "config/common/test/*" $testDirectory
    monkeyPatchPackageNamesForTestFiles $packageName $testDirectoryRoot
}

function monkeyPatchPackageNamesForTestFiles()
{
    packageName=$1
    testDirectoryRoot=$2
    for file in $testDirectoryRoot/$packageName/* ; do
        grep --quiet "package REPLACE_ME" $file && sed --expression="s/package REPLACE_ME/package com.chewielouie.$packageName/" $file > $file.tmp && mv $file.tmp $file
    done
}

function copyFilesFromConfigArea()
{
    configArea=$1
    copy config/$configArea/AndroidManifest.xml .
    copy config/$configArea/res .
    copy config/$configArea/src .
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

