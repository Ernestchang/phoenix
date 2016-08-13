# -*- coding:utf-8 -*-
from bs4 import BeautifulSoup
import re
import urlparse
import sqlite3

class HtmlParser(object):

    def _get_new_urls(self, page_url, soup):
        new_urls = set()

        # /type.aspx?p=3&c=%e5%85%83%e4%bb%a3&x=%e6%9b%b2
        links = soup.find('div', class_='pages').find_all('a')
        for link in links:
            new_url = link['href']
            # 将new_url按照page_url的格式拼接成一个新的url
            new_full_url = urlparse.urljoin(page_url, new_url)
            new_urls.add(new_full_url)

        return new_urls

    def _get_new_data(self, soup):
        ## get data
        conn = sqlite3.connect('../../../Poem/app/src/main/assets/poem.db')

        # <div class="main3"><div class="typeleft"><div class="sons"></div></div></div>
        poems = soup.find_all('div', class_='sons')
        for poem in poems:
            #print 'hello'
            author_node = poem.find('p').find_next_sibling()
            text = author_node.get_text()
            # 去除 &nbsp 造成的异常
            text = text.split(u'作者：')[1]
            text = text.split(u'0')[0]
            text = text.split(u'1')[0]
            text = text.split(u'2')[0]
            text = text.split(u'3')[0]
            text = text.split(u'4')[0]
            text = text.split(u'5')[0]
            text = text.split(u'6')[0]
            text = text.split(u'7')[0]
            text = text.split(u'8')[0]
            text = text.split(u'9')[0]
            text = text.split(u'(')[0]
            text = text.strip().lstrip().rstrip()
            #print text

            # 写入数据库
            count = 0
            # 查询作者是否已在数据库中
            sql = 'SELECT _id FROM author WHERE name LIKE "%%%s%%"' % text
            cursor = conn.execute(sql)
            for row in cursor:
                count = count + 1

            # 如果没有在数据库中，写入数据库
            if count == 0:
                sql = 'INSERT INTO author (name) VALUES ("%s")' % text
                conn.execute(sql)
                conn.commit()

        conn.close()


    def parse(self, page_url, html_cont):
        if page_url is None or html_cont is None:
            return

        soup = BeautifulSoup(html_cont, 'html.parser', from_encoding='UTF-8')
        new_urls = self._get_new_urls(page_url, soup)
        self._get_new_data(soup)

        return new_urls
