#!/bin/bash
for k in `seq 1 1 1`; do
    for i in `seq 0.002 0.0001 0.003`; do
        j=0.19
        #for j in `seq 0.5 0.1 1`; do
            echo $i $j
            java -Xmx5g -jar target/spamfilter-learning_2.10-0.1.0-jar-with-dependencies.jar ../../traindata/ runlogs/classifier-$i-$j-$k $i $j | tee runlogs/training-$i-$j-$k
        #done
    done
done
