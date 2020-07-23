# Assignment3

>Reference textbook: Computer Networking: A Top-Down Approach, 7th Edition, by James F. Kurose and Keith W. Ross, published by Pearson Education, Inc., 2017, ISBN 9780133594140.

*All homework assignments must be completed by each student individually. Any copying of someone else's work, or misrepresentation of other work as your own, will be grounds for failing this assignment or the course.*

Penalty for late work is 20 points per day late.

## There are 6 questions; make sure you answer all these questions.

1. In protocol *rdt3.0*, the ACK packets flowing from the receiver to the sender do not have sequence numbers (although they do have an ACK field that contains the sequence number of the packet they are acknowledging). Why is it that our ACK packets do not require sequence numbers?


2. Suppose that the five measured *SampleRTT* values (see Section 3.5.3) are 146 ms, 110 ms, 135 ms, 85 ms, and 92 ms. Compute the *EstimatedRTT* after each of these *SampleRTT* values is obtained, using a value of α = 0.15 and assuming that the value of *EstimatedRTT* was 120 ms just before the first of these five samples were obtained. Compute also the *DevRTT* after each sample is obtained, assuming a value of β = 0.3 and assuming the value of *DevRTT* was 7 ms just before the first of these five samples was obtained. Last, compute the TCP *TimeoutInterval* after each of these samples is obtained.


3. Host A and B are communicating over a TCP connection, and Host B has already received all bytes up through byte 473. Suppose Host A then sends two segments to Host B back-to-back. The first and second segments contain 520 and 50 bytes of data, respectively. In the first segment, the sequence number is 474, the port number is 2350, and the destination port number is 4270. Host B send an acknowledgement whenever it receives a segment from Host A.
  
  + a. In the second segment sent from Host A to Host B, what are the sequence number, source port number, and destination port number?
  
  + b. If the second segment arrives before the first segment, in the acknowledgement of the first arriving segment, what is the acknowledgement number?
  
  + c. If the first segment arrives before the second segment, in the acknowledgement of the first arriving segment, what are the acknowledgement number, the source port number, and the destination port number?
  
  + d. Suppose the two segments sent by A arrive in order at B. The first acknowledgement is lost and the second acknowledgement arrives after the first timeout interval. Draw a timing diagram, showing these segments and all other segments and acknowledgements sent. (Assume there is no additional packet loss.) For each segment in your figure, provide the sequence number and the number of bytes of data; for each acknowledgement that you add, provide the acknowledgement number.

4. Consider the GBN and SR protocols. Suppose the sequence number space is of size X. What is the largest allowable sender window that will avoid the occurrence of problems such as that in Figure 3.27 in the above textbook for each of these protocols?

5. Consider a TCP connection has an initial Threshold of 24 kB and a Maximum Segment Size (MSS) of 4 kB. The receiver advertised window is 40 kB. Suppose all transmission attempts are successful except for a triple duplicate ACK received (for the same previously transmitted data) on the *number 7* transmission and a timeout at transmission *number 12*. The first transmission attempt is number 0. Find the size of the sender’s congestion window for the first 18 transmission attempts (number 0-17) assuming the sender’s TCP implementation is using the slow-start congestion control scheme.

6. Compare GBN, SR and TCP (no delayed ACK). Assume that the timeout values for all three protocols are sufficiently long such that 10 consecutive data segments and the corresponding ACKs can be received (if not lost in the channel) by the receiving host (Host B) and the sending host (Host A) respectively. Suppose Host A sends 10 data segments to Host B, and the fifth segment (sent from A) is lost. In the end, all 10 data segments have been correctly received by Host B.

  + a. How many segments has Host A sent in total and how many ACKs has Host B sent in total? What are their sequence numbers? Answer this question for all three protocols.
  
  + b. If the timeout values for all three protocols are much longer than 10 RTT, then which protocol successfully delivers all 10 data segments in the shortest time interval? 
