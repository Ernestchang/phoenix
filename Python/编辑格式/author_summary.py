# -*- coding:utf-8 -*-

import sqlite3
import re

# open database
conn = sqlite3.connect('../../Poem/app/src/main/assets/poem.db')

# get summary list
cursor = conn.execute('SELECT name, intro FROM author')
for row in cursor:
    # get summary
    name = row[0]
    summary = row[1]

    # if summary is empty, continue
    if not summary:
        continue

    ## get format summary
    new_summary = summary

    # 1. remove head \n
    while (new_summary.find('\n') == 0):
        new_summary = new_summary.replace('\n', '', 1)

    # 2. remove tail \n
    while (new_summary[-1] == '\n'):
        new_summary = new_summary[0:-1]

    # update summary
    if not new_summary == summary:
        print 'do not same'
        conn.execute('UPDATE author SET intro = \"' + new_summary + '\" WHERE name LIKE \"%' + name + '%\"')
        conn.commit()
    else:
        pass
        #print 'same'

# close database
conn.close()
