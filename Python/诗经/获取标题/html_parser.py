# -*- coding:utf-8 -*-
from bs4 import BeautifulSoup
import re
import urlparse
import sqlite3

class HtmlParser(object):

    def _get_new_data(self, soup):
        ## get data

        conn = sqlite3.connect('../../../Poem/app/src/main/assets/poem.db')

        blocks = soup.find('div', class_='main3').find('div', class_='leftlei').find_all('div', class_='son2')
        for block in blocks:
            # 获取 风、国风、秦风及其id
            span = block.find('span')
            text = span.get_text()
            # 国风·周南：
            text = text.split(u'：')[0]
            array = text.split(u'·')
            size = len(array)
            sub1 = ''
            sub2 = ''
            sub3 = ''
            sub2 = array[0]
            if size == 1:
                sub3 = sub2
            else:
                sub3 = array[1]
            sub1 = sub2[-1]
            #print '%s %s %s' % (sub1, sub2, sub3)

            sub1_id = 0
            sub2_id = 0
            sub3_id = 0

            sql = 'SELECT _id FROM collection WHERE name == "%s"' % (sub1)
            cursor = conn.execute(sql)
            for row in cursor:
                sub1_id = row[0]
                #print sub1_id

            sql = 'SELECT _id FROM collection WHERE name == "%s"' % (sub2)
            cursor = conn.execute(sql)
            for row in cursor:
                sub2_id = row[0]
                #print sub2_id

            sql = 'SELECT _id FROM collection WHERE name == "%s"' % (sub3)
            cursor = conn.execute(sql)
            for row in cursor:
                sub3_id = row[0]
                #print sub3_id

            #print '%d %d %d' % (sub1_id, sub2_id, sub3_id)

            # 获取标题并写入数据库
            links = block.find_all('a')
            for link in links:
                text = link.get_text()
                title = text
                # 去除·
                array = text.split(u'·')
                if len(array) == 2:
                    title = array[1]
                # 跳过已佚篇
                if u'今佚' in title:
                    continue
                
                # 写入数据库
                sql = 'INSERT INTO poems (title, shijing_sub1, shijing_sub2, shijing_sub3) VALUES ("%s", %d, %d, %d)' % (title, sub1_id, sub2_id, sub3_id)
                #print sql
                conn.execute(sql)
                conn.commit()

        ## close database
        conn.close()

    def parse(self, html_cont):
        if html_cont is None:
            return

        soup = BeautifulSoup(html_cont, 'html.parser', from_encoding='UTF-8')
        self._get_new_data(soup)
