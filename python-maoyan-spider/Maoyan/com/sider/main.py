# -*- coding: utf-8 -*-
import os
import re
from sys import argv
import xml.dom.minidom as xmldom
import lxml
from bs4 import BeautifulSoup
import requests
from fontTools.ttLib import TTFont




# 请求头设置
header = {
    'Accept': '*/*;',
    'Connection': 'keep-alive',
    'Accept-Language': 'zh-CN,zh;q=0.9',
    'Accept-Encoding': 'gzip, deflate, br',
    'Host': 'maoyan.com',
    'Referer': 'http://maoyan.com/',
    'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36'
}

# 下载请求电影页面的woff字体到本地
def downfont(url):
    r = requests.get('http://'+url)
    with open(r"C:\Users\Administrator\Desktop\python-maoyan-spider\Maoyan\com\sider\demo.woff", "wb") as code:
        code.write(r.content)
    font = TTFont(r"C:\Users\Administrator\Desktop\python-maoyan-spider\Maoyan\com\sider\demo.woff")
    font.saveXML(r'C:\Users\Administrator\Desktop\python-maoyan-spider\Maoyan\com\sider\to.xml')


def findstar(titles):
    # 加载字体模板
    num = [8,6,2,1,4,3,0,9,5,7]
    data = []
    new_font = []
    xmlfilepath_temp = os.path.abspath(r"C:\Users\Administrator\Desktop\python-maoyan-spider\Maoyan\com\sider\temp.xml")
    domobj_temp = xmldom.parse(xmlfilepath_temp)
    elementobj_temp = domobj_temp.documentElement
    subElementObj = elementobj_temp.getElementsByTagName("TTGlyph")
    for i in range(len(subElementObj)):
        rereobj = re.compile(r"name=\"(.*)\"")
        find_list = rereobj.findall(str(subElementObj[i].toprettyxml()))
        data.append(str(subElementObj[i].toprettyxml()).replace(find_list[0],'').replace("\n",''))

    #根据字体模板解码本次请求下载的字体
    xmlfilepath_find = os.path.abspath(r"C:\Users\Administrator\Desktop\python-maoyan-spider\Maoyan\com\sider\to.xml")
    domobj_find = xmldom.parse(xmlfilepath_find)
    elementobj_find = domobj_find.documentElement
    tunicode = elementobj_find.getElementsByTagName("TTGlyph")
    for i in range(len(tunicode)):
        th = tunicode[i].toprettyxml()
        report = re.compile(r"name=\"(.*)\"")
        find_this = report.findall(th)
        get_code = th.replace(find_this[0], '').replace("\n", '')
        for j in range(len(data)):
            if get_code==data[j]:
                new_font.append(num[j])

    font = TTFont(r"C:\Users\Administrator\Desktop\python-maoyan-spider\Maoyan\com\sider\demo.woff")
    font_list = font.getGlyphNames()
    font_list.remove('glyph00000')
    font_list.remove('x')
    for i in range(len(font_list)) :
        font_list[i] = str(font_list[i]).lower().replace("uni",'')
    return (new_font,font_list)


def web(url):
    db_data = requests.get(url, headers=header)
    soup = BeautifulSoup(db_data.text.replace("&#x",""), 'lxml')

    titles = soup.select('body > div.banner > div > div.celeInfo-right.clearfix > div.movie-brief-container > h3')
    star = soup.select(
        'body > div.banner > div > div.celeInfo-right.clearfix > div.movie-stats-container > div > div > span > span')
    woffs = soup.select('head > style')
    people = soup.select(
        'body > div.banner > div > div.celeInfo-right.clearfix > div.movie-stats-container > div > div > div > span > span')

    # 获得字体路径
    wotflist = str(woffs[0]).split('\n')
    maoyanwotf = wotflist[5].replace(' ', '').replace('url(\'//', '').replace('format(\'woff\');', '').replace('\')',
                                                                                                               '')
    downfont(maoyanwotf)
    # 解析编码
    (new_font, font_list) = findstar(titles)

    try:
        #评分人数
        people_number = re.findall(re.compile(r">(.*)<"), str(people[0]))[0].replace(';','')
        for i in range(len(font_list)):
            if font_list[i] in people_number:
                people_number = str(people_number).replace(font_list[i],str(new_font[i]))
    except:
        people_number = 0

    print people_number
    f = open(r'C:\Users\Administrator\Desktop\python-maoyan-spider\Maoyan\com\sider\test.txt', 'w')
    f.write(people_number+'\n')

    # 评分
    try:
        star_woff = re.findall(re.compile(r">(.*)<"), str(star[0]))[0].replace(';', '')
        for i in range(len(font_list)):
            if font_list[i] in star_woff:
                star_woff = str(star_woff).replace(font_list[i], str(new_font[i]))
    except:
        star_woff = 0

    print star_woff
    f.write(star_woff+'\n')
    f.close()



def setCsv():
    url = argv[1]
    web(url)


if __name__ == '__main__':
    setCsv()  # str为标签名
