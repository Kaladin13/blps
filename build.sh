#!/bin/bash
# admin panel activeMQ
ssh -L 8161:localhost:8161 -i ~/.ssh/cloud rusilee@158.160.133.22 &
# prod db
ssh -L 6432:localhost:5432 -i ~/.ssh/cloud rusilee@158.160.133.22 &
# activeMQ topic
ssh -L 5672:localhost:5672 -i ~/.ssh/cloud rusilee@158.160.133.22 &