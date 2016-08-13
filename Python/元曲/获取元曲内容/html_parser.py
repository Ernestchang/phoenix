# -*- coding:utf-8 -*-
from bs4 import BeautifulSoup
import re
import urlparse
import sqlite3

class HtmlParser(object):

    def _get_new_urls(self, page_url, soup):
        new_urls = set()
        new_poem_urls = set()

        # /type.aspx?p=3&c=%e5%85%83%e4%bb%a3&x=%e6%9b%b2
        links = soup.find('div', class_='pages').find_all('a')
        for link in links:
            new_url = link['href']
            # 将new_url按照page_url的格式拼接成一个新的url
            new_full_url = urlparse.urljoin(page_url, new_url)
            new_urls.add(new_full_url)

        # 获取元曲详情的链接
        poems = soup.find_all('div', class_='sons')
        for poem in poems:
            link = poem.find('p').find('a')
            poem_url = link['href']
            new_poem_full_url = urlparse.urljoin(page_url, poem_url)
            new_poem_urls.add(new_poem_full_url)

        return new_urls, new_poem_urls

    def _get_new_data(self, soup):
        ## get data

        # <div class="son1"><h1>**</h1></div>
        # 千秋岁·数声鶗鴂
        # 【双调】庆东原_青田九楼山
        title_node = soup.find('h1')
        poem_name = title_node.get_text()
        #print poem_name
        # 替换 · 为 _
        poem_name = poem_name.replace(u'·', '*')
        poem_name = poem_name.replace(u'】', '*')
        poem_name = poem_name.replace(u'【', '*')
        #print poem_name
        # 获取标题、副标题
        titles = poem_name.split('*')
        length = len(titles)
        #print length
        title = ''
        sub_title = ''
        if length == 1:
            title = titles[0]
        elif length == 2:
            title = titles[0]
            sub_title = titles[1]
        elif length == 3:
            title = titles[1]
            sub_title = titles[2]
        #print '%s %s' % (title, sub_title)

        # 获取原文和addition
        # <div class="main3"><div class="shileft"><div class="son2"></div></div></div>
        content_node = soup.find('div', class_='main3').find('div', class_='shileft').find('div', class_='son2')
        content = content_node.get_text()
        content = content.split(u'朝代：')[1]
        dynastys = content.split(u'作者：')
        dynasty_name = dynastys[0]
        content = dynastys[1]
        dynasty_name = dynasty_name.replace('\n', '')
        #print content
        authors = content.split(u'原文：')
        author_name = authors[0]
        author_name = author_name.replace('\n', '')
        content = authors[1]
        #print '%s %s' %(dynasty_name, author_name)
        # 去除空格
        content = content.strip().lstrip().rstrip()
        #print content
        # 获取addition
        addition = content.split(u'。')[0]
        addition = addition.split(u'，')[0]
        addition = addition.split(u'？')[0]
        addition = addition.split(u'、')[0]
        #print addition

        # 获取作者id，朝代id
        ## open database
        conn = sqlite3.connect('../../../Poem/app/src/main/assets/poem.db')

        ## get data
        author_id = 0
        dynasty_id = 0
        tune_id = 0
        cursor = conn.execute('SELECT _id FROM author WHERE name LIKE "%' + author_name + '%"')
        for row in cursor:
            author_id = row[0]
            #print author_id

        cursor = conn.execute('SELECT _id FROM dynasty WHERE name LIKE "%' + dynasty_name + '%"')
        for row in cursor:
            dynasty_id = row[0]
            #print dynasty_id

        cursor = conn.execute('SELECT _id FROM tune WHERE name LIKE "%' + title + '%"')
        for row in cursor:
            tune_id = row[0]
            #print author_id

        if sub_title == addition:
            sub_title = ''
        sql = 'INSERT INTO poems (dynasty_id, author_id, title, subtitle, addition, content, tune_id) VALUES (%d, %d, "%s", "%s", "%s", "%s", %d)' % (dynasty_id, author_id, title, sub_title, addition, content, tune_id)
        print sql
        conn.execute(sql)
        conn.commit()

        ## close database
        conn.close()

    def parse_data(self, html_cont):
        if html_cont is None:
            return

        soup = BeautifulSoup(html_cont, 'html.parser', from_encoding='UTF-8')
        self._get_new_data(soup)


    def parse(self, page_url, html_cont):
        if page_url is None or html_cont is None:
            return

        soup = BeautifulSoup(html_cont, 'html.parser', from_encoding='UTF-8')
        new_urls, poem_urls = self._get_new_urls(page_url, soup)

        return new_urls, poem_urls
