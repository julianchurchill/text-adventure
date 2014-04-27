#!/bin/bash

DUPLICATE_EXIT_IDS=`grep "exit id:" res/raw/model_content.txt | sort | uniq --count --repeated`
if [ "${DUPLICATE_EXIT_IDS}" != "" ]
then
    echo "Duplicate exit ids found:"
    echo ${DUPLICATE_EXIT_IDS}
    exit 1
fi

DUPLICATE_LOCATION_IDS=`grep "location id:" res/raw/model_content.txt | sort | uniq --count --repeated`
if [ "${DUPLICATE_LOCATION_IDS}" != "" ]
then
    echo "Duplicate location ids found:"
    echo ${DUPLICATE_LOCATION_IDS}
    exit 1
fi

DUPLICATE_ITEM_IDS=`grep "item id:" res/raw/model_content.txt | sort | uniq --count --repeated`
if [ "${DUPLICATE_ITEM_IDS}" != "" ]
then
    echo "Duplicate item ids found:"
    echo ${DUPLICATE_ITEM_IDS}
    exit 1
fi
