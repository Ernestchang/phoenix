# -*- coding:utf-8 -*-
import url_manager, html_downloader, html_parser
import sqlite3

# 编写总调度程序
class SpiderMain(object):
    # 构造函数中初始化各模块
    def __init__(self):
        self.urls = url_manager.UrlManager()
        self.downloader = html_downloader.HtmlDownloader()
        self.parser = html_parser.HtmlParser()

    # 遍历author表，生成需要爬取内容的作者的链接地址，加入待爬取地址列表
    def generate_url(self):
        # 打开数据库
        conn = sqlite3.connect('../../../Poem/app/src/main/assets/poem.db')

        # 查找所有条目的name, intro, profile_pic_url
        cursor = conn.execute('SELECT name, intro, profile_pic_url FROM author')

        # 遍历所有条目，如果intro或profile_pic_url为空，生成该作者的链接地址，加入待爬取地址列表
        new_urls = set()
        for row in cursor:
            title = row[0]
            summary = row[1]
            profile_url = row[2]

            if not summary:
                # summary or profile_url is null, generate url to get data
                # http://baike.baidu.com/item/%E4%B8%A5%E4%BB%81
                url = 'http://baike.baidu.com/item/' + title.encode('UTF-8')
                if url not in new_urls:
                    new_urls.add(url)

        self.urls.add_new_urls(new_urls)

        # 关闭数据库
        conn.close()

    def craw(self):
        # 记录当前爬取的是第几个url
        count = 1
        # url管理器中已经有了待爬取的url，开始启动爬虫循环
        while self.urls.has_new_url():
            # 百度百科有些网页要么没有摘要数据，要么已经无法访问，所以需要加上try catch
            try:
                new_url = self.urls.get_new_url()
                print 'craw %d : %s' % (count, new_url)
                html_cont = self.downloader.download(new_url)
                #print 'parse %s' + html_cont
                self.parser.parse(html_cont)

                # 本程序最多爬取100个页面
                if count == 300:
                    break

                count = count + 1
            except:
                print 'craw failed'

# 编写main函数
if __name__=="__main__":
    # 创建一个spider
    obj_spider = SpiderMain()
    # 获取待获取内容作者链接列表
    obj_spider.generate_url()
    # 调用spider的craw方法来启动爬虫
    obj_spider.craw()
