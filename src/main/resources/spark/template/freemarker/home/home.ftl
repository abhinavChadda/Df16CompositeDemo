<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Composite API demo</title>
        <link rel="stylesheet" type="text/css" href="styles.css" />
        <!--[if IE]>
        
        <style type="text/css">
        .clear {
          zoom: 1;
          display: block;
        }
        </style>

        
        <![endif]-->
    </head>
    <body>
    	<div class="section" id="page2"> <!-- Defining the #page section with the section tag -->

            <div class="header"> <!-- Defining the header section of the page with the appropriate tag -->
                <h1>Composite API demo</h1>
                <h3>Comparison of traditional model vs Composite API</h3>

                <div class="nav clear"> <!-- The nav link semantically marks your main site navigation -->
                    <ul>
                        <!-- <li><a href="#article1" class="traditionalButton">Traditional</a></li> -->
                        <li><input type="checkbox" name="blocking" value="true" id="blocking"><label>Individual Rows</label></li>
                        <li>
                        <select id="dataSize">
                            <option value="1">Few Records (4)</option>
                            <option value="5">More Records (20)</option>
                            <option value="15">Many Records (60)</option>
                        </select></li>
                        <li><a href="#article2" class="compositeButton">Run Demo</a></li>
                        <li><a class="increase">A+</a></li>
                        <li><a class="decrease">A-</a></li>
                        <li><a href="/demo3">Demo 3</a></li>
                    </ul>
                </div>
            </div>
            
            <div class="section" id="articles"> <!-- A new section with the articles -->
				<!-- Article 1 start -->
                <div class="line"></div>  <!-- Dividing line -->

                <div class="article" id="article1"> <!-- The new article tag. The id is supplied so it can be scrolled into view. -->
                    <h2>Traditional</h2>
                    <div class="line"></div>
                    <div class="articleBody clear" id="traditionalResponse">
                       
                    </div>
                </div>
                
				<!-- Article 1 end -->
				<!-- Article 2 start -->

                <!-- <div class="line"></div> -->
                
                <div class="article" id="article2">
                    <h2>Composite API</h2>
                    <div class="line"></div>
                    <div class="articleBody clear" id="compositeResponse">
                    
                    </div>
                </div>
				<!-- Article 2 end -->

            </div>

        <div class="footer"> <!-- Marking the footer section -->

          <div class="line"></div>
            <p>Salesforce.com</p> <!-- Change the copyright notice -->
           
            <a href="#" class="up">Go UP</a>
            <a href="https://www.salesforce.com/dreamforce/DF16/" class="by">Salesforce.com, Demo for Dreamforce 2016</a>
          </div>
		</div> <!-- Closing the #page section -->
        
        <!-- JavaScript Includes -->
        <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
        <!-- <script type="text/javascript" src="jquery.scrollTo-1.4.2/jquery.scrollTo-min.js"></script> -->
        <script type="text/javascript" src="script.js"></script>
    </body>
</html>
