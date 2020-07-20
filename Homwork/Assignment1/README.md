# Homework 1

>Reference textbook: Computer Networking: A Top-Down Approach, 7th Edition, by James F. Kurose and Keith W. Ross, published by Pearson Education, Inc., 2017, ISBN 9780133594140.

*All homework assignments must be completed by each student individually. Any copying of someone else's work, or misrepresentation of other work as your own, will be grounds for failing this assignment or the course.*

Penalty for late work is 20 points per day late.

There are 6 questions; make sure you answer all these questions.

**1. Suppose that an application repeatedly transmits data at an irregular rate, i.e. repeatedly transmit 640 Kbits of data for .3 seconds, 520 Kbits of data for the next .4 second and 480 Kbit for the next .3 second. When such an application starts, it will continually running for a relatively long period of time. Answer the following questions, briefly justifying your answers:**

+ a. Would a packet-switched or a circuit-switched network be more appropriate for this application? Why?

+ b. Suppose that a packet-switched network is used and the only traffic in this network comes from such applications as described above. Furthermore, assume that in one intermediate link that the application must use, the capacity is 1.8 Mbps. Is some form of congestion control needed? Why?


**2. Suppose users share a 15 Mbps link. Also suppose each user requires 500 kbps when transmitting, but user transmit only 15 percent of the time.**

+ a. When circuit switching is used, how many users can be supported?

+ b. For the remainder of this problem, suppose packet switching is used. Find the probability that a given user is transmitting.

+ c. Suppose there are 180 users. Find the probability that at any given time, exactly x users are transmitting simultaneously. (Hint: Use the binomial distribution.)

+ d. Find the probability that there are 41 or more users transmitting simultaneously.

**3. Consider a packet of length k which begins at end system A, travels over three links to a destination end system. These three links are connected by two packet switches. Let Li, pi, and Ti denote the length, propagation speed and the transmission rate of link i, for i=1, 2, 3. The packet switch delays each packet by tproc. Assuming no queuing delays, in term of Li, pi, and Ti, (i=1, 2, 3), and k, what is the total end-to-end delay for the packet? Suppose now the packet is 4,000 bytes, the propagation speed on the three links is 2.2 x 108 m/s, the transmission rates of all three links are 10 Mbps, the packet switch processing delay is 5 msec, the length of the first link is 2,000 km, the length of the second link is 5,000 km, and the length of the third link is 3,000 km. For these values, what is the end- to-end delay?**



**4. Suppose there are S paths between the server and the client. No two paths share any link. Figure 1 shows only one of the S paths, i.e. Path i (where i = 1, iii ..., S) which consists of N links with transmission rates R1 , R2 , ..., RN . If the server can only use one path to send to the client, what is the maximum throughput that the server can achieve? If the server can use all S paths to send data, what is the maximum throughput that the server can achieve?**

![image](https://github.com/EthanRao/COMP-4320/blob/master/Homwork/IMG/Throughput%20for%20a%20file%20transfer%20from%20server%20to%20client.jpg)

Figure 1. Throughput for a file transfer from server to client

**5. Consider the queuing delay in a router buffer. Let T denote the traffic intensity; i.e. T = Pα/R, where all packets consist of P bits, α is the average rate packets arrives at the queue in packets/sec. and R is the transmission rate in bits/sec.
Suppose that the queuing delay takes the form [TP/R(1 - T) ]for T < 1.**

  + a. Provide a formula for the total delay, i.e. the queuing delay plus the transmission delay.
  + b. Plot the total delay as a function of P/R.
  + c. Suppose ρ denote the link’s transmission rate in packets/sec. Derive a formula for the total delay in terms of α and ρ.
  
**6. In modern packet-switched networks, the source host segments long, application-layer messages (for example an image or a music file) into smaller packets and sends the packets into the network. The receiver then resembles the packets back into the original message. We refer to this as message segmentation. Figure 2 illustrates the end-to-end transport of a message with and without message segmentation. Consider a message that is 8x106 bits long needs to be sent from source to destination in Figure 2. Suppose the transmission rate of each link is 10 Mbps. Ignore propagation, processing and queuing delay.**

![image](https://github.com/EthanRao/COMP-4320/blob/master/Homwork/IMG/End-to-end%20message%20transport.jpg)

Figure 2. End-to-end message transport: (a) without message segmentation; (b) with message segmentation

  + a. Consider sending the message from source to destination without message segmentation. How long does it take to move the message from the source to the first packet switch? Keeping in mind that each switch uses store-and-forward packet switching, what is the total time to move the message from source host to destination host?
  + b. Now suppose that the message is segmented into 16,000 packets, with each packets being 500 bits long. How long does it take to move the first packet from the source host to the first switch? When the first packet is being sent from the first switch to the second switch, the second packet is being sent from the source host to the first switch. At which time will the second packet be fully received at the first switch?
  + c. How long does it take to move the file from source host to destination host when message segmentation is used? Compare this result with your answer in part (a) and comment.
  + d. Discuss the drawbacks of message segmentation.




