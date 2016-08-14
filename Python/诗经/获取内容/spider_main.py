# -*- coding:utf-8 -*-
import url_manager, html_downloader, html_parser

# 编写总调度程序
class SpiderMain(object):
    # 构造函数中初始化各模块
    def __init__(self):
        self.urls = url_manager.UrlManager()
        self.downloader = html_downloader.HtmlDownloader()
        self.parser = html_parser.HtmlParser()

    def craw(self, root_url):
        # 记录当前爬取的是第几个url
        count = 1
        # 爬取待宋词链接列表
        try:
            html_cont = self.downloader.download(root_url)
            new_urls = self.parser.parse(root_url, html_cont)
            self.urls.add_new_urls(new_urls)
        except:
            print 'craw failed'


        # url管理器中已经有了待爬取的url，开始启动爬虫循环
        while self.urls.has_new_url():
            # 百度百科有些网页要么没有摘要数据，要么已经无法访问，所以需要加上try catch
            try:
                new_url = self.urls.get_new_url()
                print 'craw %d : %s' % (count, new_url)
                html_cont = self.downloader.download(new_url)
                self.parser.parse_data(html_cont)

                # 本程序最多爬取100个页面
                if count >= 500:
                    break

                count = count + 1
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
