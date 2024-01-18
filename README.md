# Reducing Manual Efforts in Equivalence Analysis in Mutation Testing
### Published at: [...] 
### Authors: [...]

***

**Subjects**
  * bisect
  * commons-lang
  * joda-time
  * pamvotis
  * triangle
  * xstream

**Results**
  * [Link to Google Docs](https://docs.google.com/spreadsheets/d/1bXLFWKJ4ksEt8nE6b97nfsRn2y3RcNzgSQ-a-thObik/edit?usp=sharing](https://docs.google.com/spreadsheets/d/1IZLhYTNJHt3C24UBGTq5kyja1EK9awq4BL3P2QiLFxU/edit?usp=sharing))
    
#### **Replicate**

This is a step-by-step to replicate this study (in a Linux/Ubuntu environment).
  1. Download [nimrod zip](https://drive.google.com/file/d/1czyPwj6XbhyytS1kzU0bYMYSFnIvsQ2F/view?usp=sharing) file, extract to a directory, and open terminal on the nimrod directory.

  2. Build the Docker image and run the container
  ```
  $ docker build .
  ...
  Installing collected packages: soupsieve, beautifulsoup4, bs4
  Successfully installed beautifulsoup4-4.11.1 bs4-0.0.1 soupsieve-2.3.2.post1
  Removing intermediate container 9548a2f1e9b7
   ---> <IMAGE_ID>
  Successfully built <IMAGE_ID>    #You can also get the IMAGE_ID through the command docker images

  $ docker run -ti <IMAGE_ID>
  ```
  3. Get into any subject folder and execute **Nimrod**
  ```
  $ cd subjects/triangle/ [or any other subject]
  $ python run_nimrod.py
  ```
  4. Observe the standard output and check the results at the nimrod_output/ folder 

