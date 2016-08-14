# -*- coding:utf-8 -*-
from bs4 import BeautifulSoup
import re
import urlparse
import sqlite3

class HtmlParser(object):

    def _get_new_urls(self, page_url, soup):
        new_urls = set()

        blocks = soup.find('div', class_='main3').find('div', class_='leftlei').find_all('div', class_='son2')
        for block in blocks:
            links = block.find_all('a')
            for link in links:
                text = link.get_text()
                # 跳过已佚篇
                if u'今佚' in text:
                    continue
                new_url = link['href']
                # 将new_url按照page_url的格式拼接成一个新的url
                new_full_url = urlparse.urljoin(page_url, new_url)
                new_urls.add(new_full_url)

        return new_urls

    def _get_new_data(self, soup):
        ## open database
        conn = sqlite3.connect('../../../Poem/app/src/main/assets/poem.db')

        # 获取标题 title
        title = soup.find('h1').get_text()
        # 国风·王风·扬之水
        array = title.split(u'·')
        size = len(array)
        if size > 1:
            title = array[size - 1]
        #print title
        
        # 判断标题是否在数据库中，如果没有，提示异常，并返回
        sql = 'SELECT title FROM poems WHERE title == "%s"' % title
        count = 0
        cursor = conn.execute(sql)
        for row in cursor:
            count = count + 1
        if count == 0:
            print 'data base has no poem with title %s' % title
            return

        # 内容
        block = soup.find('div', class_='main3').find('div', class_='shileft').find('div', class_='son2')
        content = block.get_text().split(u'原文：')[1]
        # 去除空格
        content = content.strip().lstrip().rstrip()
        # 去除换行
        #print content

        # 写入数据库
        sql = 'UPDATE poems SET content = "%s" WHERE title == "%s"' % (content, title)
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
