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
            link = poem.find('p').find('a')
            text = link.get_text()

            # 山坡羊·潼关怀古
            # 【双调】楚天遥过清江引
            # 获取曲牌名
            title = text.split(u'·')[0]
            title = title.split(u'】')[0]
            titles = title.split(u'【')
            length = len(titles)
            if length == 2:
                title = titles[1]
            #print title

            # 写入数据库
            count = 0
            # 查询作者是否已在数据库中
            sql = 'SELECT _id FROM tune WHERE name LIKE "%%%s%%"' % title
            cursor = conn.execute(sql)
            for row in cursor:
                count = count + 1

            # 如果没有在数据库中，写入数据库
            if count == 0:
                sql = 'INSERT INTO tune (name) VALUES ("%s")' % title
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
