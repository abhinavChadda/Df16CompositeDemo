<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
    
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
                <h3>Demonstrating Transactionality in Composite API</h3>

                <div class="nav clear"> <!-- The nav link semantically marks your main site navigation -->
                    <ul>
                        <li><a class="increase">A+</a></li>
                        <li><a class="decrease">A-</a></li>
                        <li><a href="/home" >Demo 1</a></li>
                   </ul>
                </div>
            </div>
            
            <!-- Dividing line -->
            <div class="line"></div>              

            <div class="articleNew"> <!-- The new article tag. The id is supplied so it can be scrolled into view. -->
                <h2>Input</h2>
                <div class="line"></div>
                <div class="articleBody clear">
                    <label>Account Name: <input type="text" name="accountName" id="accountName" value="Salesforce.com"></label>
                    <br/>&nbsp;&nbsp;&nbsp;<label>Contact 1 LastName: <input type="text" name="accountName" id="contact1LastName" value="Benioff"></label>
                    <br/>&nbsp;&nbsp;&nbsp;<label>Contact 2 LastName: <input type="text" name="accountName" id="contact2LastName" value="Harris"></label>
                    <br/>&nbsp;&nbsp;&nbsp;<label>Contact 3 LastName: <input type="text" name="accountName" id="contact3LastName"> (Leave it blank to trigger failure mode)</label>
                    <br/><br/><div class="navFloat clear"><a href="#article2" class="transactionalityButton">Run Demo</a></div>
                </div>
            </div>

            <div class="section" id="articles" > <!-- A new section with the articles -->

                <div class="article" id="article1"> <!-- The new article tag. The id is supplied so it can be scrolled into view. -->
                    <h2>Traditional Response</h2>
                    <div class="line"></div>
                    <div class="articleBody clear" id="traditionalResponse">
                        
                    </div>
                </div>

                <div class="article" id="article2"> <!-- The new article tag. The id is supplied so it can be scrolled into view. -->
                    <h2>Composite Response</h2>
                    <div class="line"></div>
                    <div class="articleBody clear" id="compositeResponse">
                        
                    </div>
                </div>
            </div>

        <div class="footer footer2"> <!-- Marking the footer section -->

          <div class="line"></div>
            <p>Salesforce.com</p> <!-- Change the copyright notice -->
           
            <a href="#" class="up">Go UP</a>
            <a href="https://www.salesforce.com/dreamforce/DF16/" class="by">Salesforce.com, Demo for Dreamforce 2016</a>
          </div>
		</div> <!-- Closing the #page section -->
        
        <!-- JavaScript Includes -->
        <!-- <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script> -->
        <script type="text/javascript" src="//code.jquery.com/jquery-3.1.1.min.js"></script>
        <script type="text/javascript" src="script.js"></script>

        <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/jstree/3.0.9/themes/default/style.min.css" />
        <script src="//cdnjs.cloudflare.com/ajax/libs/jstree/3.0.9/jstree.min.js"></script>
    </body>
</html>
