#!/bin/bash

function usage()
{
    echo "Usage: prepare.sh <app id>"
    echo " where app id can currently be one of [1 2]"
}

function removeOldManifest()
{
    rm --force AndroidManifest.xml
}

function removeOldSourceFilesForOtherPackage()
{
    packageName=$1
    rm --force --recursive src/main/java/com/chewielouie/$packageName
    rm --force --recursive src/test/java/com/chewielouie/$packageName
}

function createNewPackage()
{
    packageName=$1
    mkdir --parents src/main/java/com/chewielouie/$packageName
    mkdir --parents src/test/java/com/chewielouie/$packageName
}

function copy()
{
    src=$1
    dest=$2
    cp --update --recursive $src $dest
}

function copyNewAndDifferingFilesFromSrcDirectoryToDestination()
{
    src=$1
    dest=$2
    echo "Copying directory contents from $src to $dest"
    cd $src && fileList=`find . -type f -print` && cd - > /dev/null
    for file in $fileList; do
        fileSrc=$src/$file
        fileDest=$dest/$file
        if [ "$fileSrc" -nt "$fileDest" ] ; then
            echo " copying newer     $fileSrc"
            cp $fileSrc $fileDest
        elif ! cmp $fileSrc $fileDest >& /dev/null ; then
            echo " copying differing $fileSrc"
            cp $fileSrc $fileDest
        fi
    done
}

function copyCommonFiles()
{
    copyNewAndDifferingFilesFromSrcDirectoryToDestination config/common/res res
}

function copyTestFilesForPackage()
{
    packageName=$1
    testDirectoryRoot=src/test/java/com/chewielouie
    testDirectory=$testDirectoryRoot/$packageName
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
    copyNewAndDifferingFilesFromSrcDirectoryToDestination config/$configArea/res res
    copyNewAndDifferingFilesFromSrcDirectoryToDestination config/$configArea/src src
}

# remove all files in res that are not in config/$configArea/res or config/common/res
function removeResourceFilesThatDoNotBelongToThisConfiguration()
{
    configArea=$1
    cd config/$configArea && find res/ -type f -print | sort | uniq >  /tmp/goodFiles.$$ && cd - > /dev/null
    cd config/common      && find res/ -type f -print | sort | uniq >> /tmp/goodFiles.$$ && cd - > /dev/null
    find res/ -type f -print | sort | uniq > /tmp/currentFiles.$$
    filesToRemove=`comm -23 <(sort /tmp/currentFiles.$$ | uniq) <(sort /tmp/goodFiles.$$ | uniq)`
    echo "Removing resource files that do not belong in this configuration"
    rm /tmp/goodFiles.$$ /tmp/currentFiles.$$
    for file in $filesToRemove ; do
        echo " removing $file"
        rm --force $file
    done
}

if [ -z "$1" ]
then
    usage
    exit 1
fi

case "$1" in
    1)  echo "Configuring app id 1"
        removeOldManifest
        removeOldSourceFilesForOtherPackage textadventure2
        createNewPackage textadventure
        copyCommonFiles
        copyTestFilesForPackage textadventure
        copyFilesFromConfigArea 1
        removeResourceFilesThatDoNotBelongToThisConfiguration 1
        ;;
    2)  echo "Configuring app id 2"
        removeOldManifest
        removeOldSourceFilesForOtherPackage textadventure
        createNewPackage textadventure2
        copyCommonFiles
        copyTestFilesForPackage textadventure2
        copyFilesFromConfigArea 2
        removeResourceFilesThatDoNotBelongToThisConfiguration 2
        ;;
    *)  usage
        ;;
esac

