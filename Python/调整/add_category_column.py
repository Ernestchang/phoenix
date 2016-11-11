# -*- coding:utf-8 -*-

import sqlite3
import re

# open database
conn = sqlite3.connect('../../Classic/app/src/main/assets/classic.db')

# get shijing_sub3 name in table poem, if value > 0, do follow;
# get category_id of sub3 name in table category;
# update column category_id in table poem.
cursor = conn.execute('SELECT p._id, p.genre_id FROM poems p WHERE p.collection_id = 8')
for row in cursor:
    # get summary
    pid = row[0]
    gid = row[1]
    # print 'id is %d' % pid

    categoryId = 0

    if gid == 1:
    	categoryId = 201;
    elif gid == 2:
    	categoryId = 202;
    elif gid == 3:
    	categoryId = 203;
    elif gid == 4:
    	categoryId = 204;
    elif gid == 5:
    	categoryId = 205;
    elif gid == 6:
    	categoryId = 206;
    elif gid == 7:
    	categoryId = 207;

    sql = 'UPDATE poems SET category_id = %d WHERE _id = %d' % (categoryId, pid)
    # print sql
    conn.execute(sql)
    conn.commit()

    #cursor_2 = conn.execute('SELECT _id FROM category WHERE name LIKE \"%' + name + '%\" AND _id > 68')
    #for row_2 in cursor_2:
    #	id = row_2[0]
    	# print 'name is %s, id is %d' % (name, id)

    	# update category id
    #	sql = 'UPDATE poems SET category_id = %d WHERE _id = %d' % (id, pid)
    	# print sql
    #	conn.execute(sql)
    #	conn.commit()

# close database
print 'done'
conn.close()
