# -*- coding:utf-8 -*-

class UrlManager(object):

    def __init__(self):
        self.new_urls = set()
        self.old_urls = set()
        self.new_poem_urls = set()
        self.old_poem_urls = set()

    def add_new_url(self, url):
        if url is None:
            return
        if url not in self.new_urls and url not in self.old_urls:
            self.new_urls.add(url)

    def add_new_urls(self, urls):
        if urls is None or len(urls) == 0:
            return
        for url in urls:
            self.add_new_url(url)

    def add_new_poem_url(self, url):
        if url is None:
            return
        if url not in self.new_poem_urls and url not in self.old_poem_urls:
            self.new_poem_urls.add(url)

    def add_new_poem_urls(self, urls):
        if urls is None or len(urls) == 0:
            return
        for url in urls:
            self.add_new_poem_url(url)

    def has_new_url(self):
        return len(self.new_urls) != 0

    def get_new_url(self):
        new_url = self.new_urls.pop()
        self.old_urls.add(new_url)
        return new_url

    def has_new_poem_url(self):
        return len(self.new_poem_urls) != 0

    def get_new_poem_url(self):
        new_url = self.new_poem_urls.pop()
        self.old_poem_urls.add(new_url)
        return new_url
