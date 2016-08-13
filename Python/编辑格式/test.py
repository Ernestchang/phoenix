# -*- coding:utf-8 -*-

import re

#str1 = 'hello[3] world[2] '

#strinfo = re.compile(r'\[\d\] ')
#b = strinfo.subn('', str1)[0]
#print b

#ref=re.compile(r"/view/\d+\.htm"))
#a = 'hello world'
#strinfo = re.compile('world')
#b = strinfo.subn('python',a)
#print b

new_summary = '杜秋（约791—？），《资治通鉴》称杜仲阳[1] ，后世多称为“杜秋娘”，是唐代金陵人。15岁时成了李锜的妾侍。'
regex = re.compile(r'\[\d+\] ')
new_summary = regex.subn('', new_summary)[0]
print new_summary
