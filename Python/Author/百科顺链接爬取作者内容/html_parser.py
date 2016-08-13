# -*- coding:utf-8 -*-
from bs4 import BeautifulSoup
import re
import urlparse
import sqlite3

class HtmlParser(object):

    def _get_new_urls(self, page_url, soup):
        new_urls = set()
        conn = sqlite3.connect('../../../Poem/app/src/main/assets/poem.db')

        # /view/123.htm
        links = soup.find_all('a', href=re.compile(r"/view/\d+\.htm"))
        for link in links:
            poem_name = link.get_text()

            # 查询是否在作者列表中
            result_count = 0;
            cursor = conn.execute('SELECT name FROM author WHERE name LIKE \"%' + poem_name + '%\"')
            for row in cursor:
                result_count = result_count + 1
            
            # 如果链接名在作者列表中，才加入爬取列表
            if (result_count == 0):
                continue

            # 是作者的链接，添加链接到缓存中
            new_url = link['href']
            # 将new_url按照page_url的格式拼接成一个新的url
            new_full_url = urlparse.urljoin(page_url, new_url)
            new_urls.add(new_full_url)

        # /subview/16967/6250840.htm
        links = soup.find_all('a', href=re.compile(r"/subview/\d+/\d+\.htm"))
        for link in links:
            poem_name = link.get_text()

            # 查询是否在作者列表中
            result_count = 0;
            cursor = conn.execute('SELECT name FROM author WHERE name LIKE \"%' + poem_name + '%\"')
            for row in cursor:
                result_count = result_count + 1

            # 如果链接名在作者列表中，才加入爬取列表
            if (result_count == 0):
                continue

            # 是作者的链接，添加链接到缓存中
            new_url = link['href']
            # 将new_url按照page_url的格式拼接成一个新的url
            new_full_url = urlparse.urljoin(page_url, new_url)
            new_urls.add(new_full_url)

        conn.close()
        return new_urls

    def _get_new_data(self, page_url, soup):
        ## get data

        # <dd class="lemmaWgt-lemmaTitle-title"><h1>**</h1></dd>
        title_node = soup.find('dd', class_='lemmaWgt-lemmaTitle-title').find('h1')
        poem_name = title_node.get_text().encode('UTF-8', 'ignore')

        # <div class="lemma-summary">**</div>
        summary_node = soup.find('div', class_='lemma-summary')
        summary = summary_node.get_text().encode('UTF-8', 'ignore')

        # <div class="summary-pic"><img src="..."></div>
        img_node = soup.find('div', class_='summary-pic').find('img')
        imgUrl = img_node['src'].encode('UTF-8', 'ignore')

        ## open database
        conn = sqlite3.connect('../../../Poem/app/src/main/assets/poem.db')

        ## update data
        conn.execute('UPDATE author SET intro = \"' + summary + '\", profile_pic_url = \"' + imgUrl + '\" WHERE name LIKE \"%' + poem_name + '%\"')
        conn.commit()

        ## close database
        conn.close()

    def parse(self, page_url, html_cont):
        if page_url is None or html_cont is None:
            return

        soup = BeautifulSoup(html_cont, 'html.parser', from_encoding='UTF-8')
        new_urls = self._get_new_urls(page_url, soup)
        self._get_new_data(page_url, soup)

        return new_urls
