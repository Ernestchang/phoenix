# -*- coding:utf-8 -*-

import sqlite3
import re

# open database
conn = sqlite3.connect('../../Classic/app/src/main/assets/classic.db')

# get shijing_sub3 name in table poem, if value > 0, do follow;
# get category_id of sub3 name in table category;
# update column category_id in table poem.
cursor = conn.execute('SELECT p._id, c.name FROM poems p JOIN collection c ON p.shijing_sub3 = c._id')
for row in cursor:
    # get summary
    pid = row[0]
    name = row[1]
    #print 'name is %s' % name

    cursor_2 = conn.execute('SELECT _id FROM category WHERE name LIKE \"%' + name + '%\" AND _id > 68')
    for row_2 in cursor_2:
    	id = row_2[0]
    	# print 'name is %s, id is %d' % (name, id)

    	# update category id
    	sql = 'UPDATE poems SET category_id = %d WHERE _id = %d' % (id, pid)
    	# print sql
    	conn.execute(sql)
    	conn.commit()

# close database
print 'done'
conn.close()
