# -*- coding:utf-8 -*-
import html_downloader, html_parser

# 编写总调度程序
class SpiderMain(object):
    # 构造函数中初始化各模块
    def __init__(self):
        self.downloader = html_downloader.HtmlDownloader()
        self.parser = html_parser.HtmlParser()

    def craw(self, root_url):
        # 爬取网页中标题
        try:
            html_cont = self.downloader.download(root_url)
            self.parser.parse(html_cont)
        except:
            print 'craw failed'

# 编写main函数
if __name__=="__main__":
    # 在main函数中设置要爬取的入口url
    root_url = "http://so.gushiwen.org/gushi/shijing.aspx"
    # 创建一个spider
    obj_spider = SpiderMain()
    # 调用spider的craw方法来启动爬虫
    obj_spider.craw(root_url)
