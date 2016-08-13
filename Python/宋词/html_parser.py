# -*- coding:utf-8 -*-
from bs4 import BeautifulSoup
import re
import urlparse
import sqlite3

class HtmlParser(object):

    def _get_new_urls(self, page_url, soup):
        new_urls = set()

        conn = sqlite3.connect('../../Poem/app/src/main/assets/poem.db')

        # /view_47069.aspx
        blocks = soup.find_all('div', class_='son2')
        for block in blocks:
            links = block.find_all('a', href=re.compile(r"/view_\d+\.aspx"))
            for link in links:
                new_url = link['href']
                # 将new_url按照page_url的格式拼接成一个新的url
                new_full_url = urlparse.urljoin(page_url, new_url)
                poem = link.find_parent()
                # 一剪梅·红藕香残玉簟(李清照)
                poem_title = poem.get_text()
                #print poem_title
                # 去除空格
                poem_title = poem_title.strip().lstrip().rstrip()
                #print poem_title
                # 替换· ( ) 为 _
                poem_title = poem_title.replace(u'·', '_').replace(u'(', '_').replace(u')', '_')
                #print poem_title
                # 分割
                params = poem_title.split('_')
                length = len(params)
                # 获取标题、副标题、作者姓名
                title = ''
                sub_title = ''
                author_name = ''
                sql = ''
                if length == 2:
                    # 鹧鸪天_己酉之秋苕溪记所见 title_subtitle
                    title = params[0]
                    sub_title = params[1]
                    sql = 'SELECT p._id FROM poems p JOIN author a ON p.author_id = a._id WHERE p.title LIKE "%' + title + '%" AND (p.subtitle LIKE "%' + sub_title + '%" OR p.addition LIKE "%' + sub_title + '%")'
                elif length == 3:
                    # 满庭芳_周邦彦_  title_author
                    title = params[0]
                    author_name = params[1]
                    sql = 'SELECT p._id FROM poems p JOIN author a ON p.author_id = a._id WHERE p.title LIKE "%' + title + '%" AND a.name LIKE "%' + author_name + '%"'
                elif length == 4:
                    # 瑞龙吟_大石春景_周邦彦_  title_subtitle_author
                    title = params[0]
                    sub_title = params[1]
                    author_name = params[2]
                    sql = 'SELECT p._id FROM poems p JOIN author a ON p.author_id = a._id WHERE p.title LIKE "%' + title + '%" AND (p.subtitle LIKE "%' + sub_title + '%" OR p.addition LIKE "%' + sub_title + '%") AND a.name LIKE "%' + author_name + '%"'
                #print '%s %s %s %s' % (title, sub_title, author_name, new_full_url)
                # 查询poem表中是否已含有该宋词
                result_count = 0;

                cursor = conn.execute(sql)
                for row in cursor:
                    result_count = result_count + 1
                if result_count > 0:
                    # 该宋词已添加到poem表中，检查下一首宋词
                    #print '%s in poem' % title
                    continue
                else:
                    # 该宋词未添加到poem表中，把链接加入待爬取列表中
                    #print sql
                    #print '%s %s %s not in poem' % (title, sub_title, author_name)
                    new_urls.add(new_full_url)

        conn.close()
        return new_urls

    def _get_new_data(self, soup):
        ## get data

        # <div class="son1"><h1>**</h1></div>
        # 千秋岁·数声鶗鴂
        title_node = soup.find('h1')
        poem_name = title_node.get_text()#.encode('UTF-8', 'ignore')
        #print poem_name
        # 替换 · 为 _
        poem_name = poem_name.replace(u'·', '_')
        #print poem_name
        # 获取标题、副标题
        titles = poem_name.split('_')
        length = len(titles)
        title = ''
        sub_title = ''
        if length == 1:
            title = titles[0]
        elif length == 2:
            title = titles[0]
            sub_title = titles[1]
        #print '%s %s' % (title, sub_title)

        # 获取原文和addition
        # <div class="main3"><div class="shileft"><div class="son2"></div></div></div>
        content_node = soup.find('div', class_='main3').find('div', class_='shileft').find('div', class_='son2')
        content = content_node.get_text()
        content = content.split(u'朝代：')[1]
        dynastys = content.split(u'作者：')
        dynasty_name = dynastys[0]
        dynasty_name = dynasty_name.replace('\n', '')
        content = dynastys[1]
        #print content
        authors = content.split(u'原文：')
        author_name = authors[0]
        author_name = author_name.replace('\n', '')
        content = authors[1]
        #print '%s%s' %(dynasty_name, author_name)
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
        conn = sqlite3.connect('../../Poem/app/src/main/assets/poem.db')

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
        #print sql
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
        new_urls = self._get_new_urls(page_url, soup)

        return new_urls
