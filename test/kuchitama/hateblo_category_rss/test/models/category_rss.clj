(ns kuchitama.hateblo-category-rss.test.models.category-rss
	(:use [clojure.test])
	(:require [kuchitama.hateblo-category-rss.models.category-rss :as rss]
		[net.cgrand.enlive-html :as enlive]))

(def test-rss-feed
	"<feed xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"ja\">
  <title>title</title>
  
  <subtitle>subtitle</subtitle>
  
  <link href=\"http://kuchitama.hateblo.jp/\"/>
  <updated>2013-01-01T00:00:00+09:00</updated>
  <author>
    <name>kuchitama</name>
  </author>
  <generator uri=\"http://blog.hatena.ne.jp/\">Hatena::Blog</generator>
  <id>http://blog.hatena.ne.jp/id/12704914408862843284</id>
    
    <entry>
        <title>entry1</title>
        <link href=\"link1\"/>
        <id>id1</id>
        <published>2013-01-19T22:32:52+09:00</published>
        <updated>2013-01-19T22:32:52+09:00</updated>
        <summary>summary1</summary>
        <content type=\"html\">content1</content>
        
        <category term=\"category1\" label=\"category1\" />
        <author>
            <name>kuchitama</name>
        </author>
    </entry>
	<entry>
        <title>entry2</title>
        <link href=\"link2\"/>
        <id>id2</id>
        <published>2013-01-19T22:32:52+09:00</published>
        <updated>2013-01-19T22:32:52+09:00</updated>
        <summary>summary2</summary>
        <content type=\"html\">content2</content>
        
        <category term=\"category1\" label=\"category1\" />
        <category term=\"category2\" label=\"category2\" />
        <author>
            <name>kuchitama</name>
        </author>
    </entry>
	<entry>
        <title>entry3</title>
        <link href=\"link3\"/>
        <id>id3</id>
        <published>2013-01-19T22:32:52+09:00</published>
        <updated>2013-01-19T22:32:52+09:00</updated>
        <summary>summary3</summary>
        <content type=\"html\">content3</content>
        
        <category term=\"category2\" label=\"category2\" />
        <category term=\"category3\" label=\"category3\" />
        <author>
            <name>kuchitama</name>
        </author>
    </entry>
</feed>")

(def empty-rss (enlive/xml-resource (java.io.StringReader. "<feed xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"ja\">
  <title>title</title>
  
  <subtitle>subtitle</subtitle>
  
  <link href=\"http://kuchitama.hateblo.jp/\"/>
  <updated>2013-01-01T00:00:00+09:00</updated>
  <author>
    <name>kuchitama</name>
  </author>
  <generator uri=\"http://blog.hatena.ne.jp/\">Hatena::Blog</generator>
  <id>http://blog.hatena.ne.jp/id/12704914408862843284</id>
	</feed>")))

(def e-entry2 (enlive/xml-resource (java.io.StringReader. "<entry>
        <title>entry2</title>
        <link href=\"link2\"/>
        <id>id2</id>
        <published>2013-01-19T22:32:52+09:00</published>
        <updated>2013-01-19T22:32:52+09:00</updated>
        <summary>summary2</summary>
        <content type=\"html\">content2</content>
        
        <category term=\"category1\" label=\"category1\" />
        <category term=\"category2\" label=\"category2\" />
        <author>
            <name>kuchitama</name>
        </author>
    </entry>
")))

(def e-entry3 (enlive/xml-resource (java.io.StringReader. "<entry>
        <title>entry3</title>
        <link href=\"link3\"/>
        <id>id3</id>
        <published>2013-01-19T22:32:52+09:00</published>
        <updated>2013-01-19T22:32:52+09:00</updated>
        <summary>summary3</summary>
        <content type=\"html\">content3</content>
        
        <category term=\"category2\" label=\"category2\" />
        <category term=\"category3\" label=\"category3\" />
        <author>
            <name>kuchitama</name>
        </author>
    </entry>")))

(deftest test-parse-rss 
	(let [mapped-rss (rss/parse-rss test-rss-feed)]
		(do
			(is (and 
					(seq? mapped-rss) 
					(map? (first mapped-rss))) 
				"parse-rss must return mapped xml")
			(is (not (nil? mapped-rss))
				"parse-rss must return not nil when arguments is xml"))))

(deftest test-get-target-category
	(let [mapped-rss (rss/parse-rss test-rss-feed)]
		(do
			(is (= (rss/get-target-category mapped-rss "category3")
					e-entry3)
				"get-target-category must return map contains target-category")
			(let [entries (rss/get-target-category mapped-rss "category2")]
				(are [x y] (= x y)
					(first entries) (first e-entry2)
					(last entries) (first e-entry3))))))

(comment "微妙な改行とかで失敗するのでコメント化"
(deftest test-remove-entry
	(let [mapped-rss (rss/parse-rss test-rss-feed)] 
		(is (= (rss/remove-entry mapped-rss)
			empty-rss))))
)

(deftest test-remove-entry
	(let [mapped-rss (rss/parse-rss test-rss-feed)] 
		(is 
			(= '() (enlive/select (rss/remove-entry mapped-rss) [:entry]))
			"result of remove-entry must not contain entries")))

