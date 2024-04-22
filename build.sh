#!/bin/bash
# 8161 - admin panel activeMQ
# 6432 - prod db
# 5672 - activeMQ topic
ssh -L 8161:localhost:8161 -L 6432:localhost:5432 -L 5672:localhost:5672 -i ~/.ssh/cloud rusilee@158.160.133.22