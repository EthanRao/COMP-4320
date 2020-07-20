# Homework 2

>Reference textbook: Computer Networking: A Top-Down Approach, 7th Edition, by James F. Kurose and Keith W. Ross, published by Pearson Education, Inc., 2017, ISBN 9780133594140

*All homework assignments must be completed by each student individually. Any copying of someone else's work, or misrepresentation of other work as your own, will be grounds for failing this assignment or the course. All homework must be submitted in hardcopy.*

Penalty for late work is 20 points per day late.

There are 6 questions; make sure you answer all the questions.

**1. Consider an HTTP client that wants to retrieve a Web document at a given URL. The IP address of the HTTP server is initially unknown. What transport and application-layer protocols besides HTTP are needed in this scenario?**



**2. Suppose within your Web browser you click on a link to obtain a Web page. The IP address for the associated URL is not cached in your local host, so a DNS lookup is necessary to obtain the IP address. Suppose that d DNS servers are visited before your host receives the IP address from DNS; the successive visits incur an RTT of RTT1, ..., RTTd. Further suppose that the Web page associated with the link contains exactly one object, consisting of a small amount of HTML text. Let RTTx denote the RTT between the local host and the server containing the object. Assume that the transmission time of the object is dt. How much time elapses from when the client clicks on the link until the client receives the object.**



**3. Referring to Problem 2 above, suppose the HTML file references seven very small objects on the same server. Neglecting transmission time, how much time elapses with**
  
  + a. Non-persistent HTTP with no parallel TCP connections?
  
  + b. Non-persistent HTTP with the browser configured for 4 parallel connections?
  
  + c. Persistent HTTP? (Assume that pipelining is used.)
  
  
  
**4. Consider a short, 50-meter link, over which a sender can transmit at a rate of 320 bits/sec in both directions. Suppose that packets containing data are 200,000 bits long, and packets containing only control (e.g. ACK or handshaking) are 180 bits long. Assume that k parallel connections each get 1/k of the link bandwidth. Now consider the HTTP protocol, and assume that each downloaded object is 200 Kbit long, and the initial downloaded object contains 8 referenced objects from the same sender. Would parallel download via parallel instances of non- persistent HTTP make sense in this case? Now consider persistent HTTP. Do you expect significant gains over the non-persistent case? Justify and explain your answer.**



**5. Consider the scenario introduced in Question (4) above. Now suppose that the link is shared by John with five other users. John uses parallel instances of non- persistent HTTP, and the other five users use non-persistent HTTP without parallel downloads.**

  + Do John’s parallel connections help him get Web pages more quickly? Why or why not?
  
  + If all six users open parallel instances of non-persistent HTTP, then would John’s parallel connections still be beneficial? Why or why not? 
  
  
  
**6. Consider the following institutional network that is connected to the Internet.**

![image](https://github.com/EthanRao/COMP-4320/blob/master/Homwork/IMG/Question6.jpg)

**Suppose that the average object size is 560,000 bits and that the average request rate from the institution’s browsers to the origin servers is 25 requests per second. Also suppose that the amount of time it take from when the router on the Internet side of the access link forwards an HTTP request until it receives the response is 2.5 seconds on average (see Section 2.2.5). Model the total average response time as the sum of the average access delay (that is, the delay from Internet router to institution router) and the average Internet delay. For the average access delay, use α/(1- αλ), where α is the average time required to send an object over the access link and λ is the arrival rate of objects to the access link.**

  + a. Find the total average response time.
  
  + b. Now suppose a cache is installed in the institutional LAN. Suppose the hit rate is 0.25. Find the total response time.
  
  
