#!/bin/bash
# 8161 - admin panel activeMQ
# 6432 - prod db
# 5672 - activeMQ topic
# 7432 - report db
ssh -L 8161:localhost:8161 -L 6432:localhost:5432 -L 5672:localhost:5672 -L 7432:localhost:6432 \
-i ~/.ssh/cloud rusilee@158.160.145.193