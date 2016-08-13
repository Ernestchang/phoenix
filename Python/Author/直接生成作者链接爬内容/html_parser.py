# -*- coding:utf-8 -*-
from bs4 import BeautifulSoup
import re
import urlparse
import sqlite3

class HtmlParser(object):

    def _get_new_data(self, soup):
        ## get data

        # <dd class="lemmaWgt-lemmaTitle-title"><h1>**</h1></dd>
        title_node = soup.find('dd', class_='lemmaWgt-lemmaTitle-title').find('h1')
        poem_name = title_node.get_text().encode('UTF-8', 'ignore')

        # <div class="lemma-summary">**</div>
        summary_node = soup.find('div', class_='lemma-summary')
        summary = summary_node.get_text().encode('UTF-8', 'ignore')

        # <div class="summary-pic"><img src="..."></div>
        imgUrl = ''
        try:
            img_node = soup.find('div', class_='summary-pic').find('img')
            imgUrl = img_node['src'].encode('UTF-8', 'ignore')
        except Exception, e:
            raise e

        ## open database
        conn = sqlite3.connect('../../../Poem/app/src/main/assets/poem.db')

        ## update data
        conn.execute('UPDATE author SET intro = \"' + summary + '\", profile_pic_url = \"' + imgUrl + '\" WHERE name LIKE \"%' + poem_name + '%\"')
        conn.commit()

        ## close database
        conn.close()

    def parse(self, html_cont):
        if html_cont is None:
            return

        soup = BeautifulSoup(html_cont, 'html.parser', from_encoding='UTF-8')
        self._get_new_data(soup)
