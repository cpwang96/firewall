# firewall
For this design, I used an arraylist to store the incoming data from the input csv file. When matching with input, the program iterates the list and tries to find a matching entry. If it couldn't find, then returns false. The running time for this for loop would be O(n). However, for the matching part, I think it could be accelerated by using regular expression.

For the testing part, I used the given five test cases tested in the main method. I wrote a parser to directly check the validity of the input in order to speed up the process inside accept_packet. It also involves a single ip parser to check if the ip address is legal.

Interested Team: Policy Team and Platform Team
