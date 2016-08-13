import sqlite3

# open database
conn = sqlite3.connect('../../../Poem/app/src/main/assets/poem.db')

conn.execute('UPDATE author SET intro = \"\", profile_pic_url = \"\"')
conn.commit()

conn.close()
