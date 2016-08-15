#!/bin/bash
cd ~/shellscripts
#batch recommend
sh downloaddata.sh
sh etl.sh
sh dataextrctor.sh
sh AlsModelTraning.sh
sh recommendMulti.sh
#steraming recommend
sh producer.sh
sh consumer.sh
