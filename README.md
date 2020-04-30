![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=particip-plugin-participatoryideation-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-participatoryideation&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-participatoryideation)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-participatoryideation&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-participatoryideation)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Aplugin-participatoryideation&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Aplugin-participatoryideation)

# Plugin participatory ideation

## Introduction

This plugin handles citizen-oriented ideation on the web.

It provides :

 
* A step-by-step form to submit a proposal
* A SOLR-based search page with thumbnails
* For each proposal, a sheet with all informations
* A back-office to manage proposals

 **The [OpenPB](https://github.com/lutece-secteur-public/particip-site-participatorybudget) demo site uses this plugin, so you can see it in action !** 

Please keep in mind that this plugin is in alpha version and is under development ; some features could not work properly.

## Usage

Above all, please refer to the 'configuration' section of the document to create data for you ideation campaign.

Before accessing the SOLR search page, you must index the pre-created proposals : go to the Solr Indexing back-office page, and click on 'Start indexing' button.

 **Search page** 

Now you can access to the search page :

```
http://test.paris.mdp:8080/pb/jsp/site/Portal.jsp?page=solrProposalSearch&conf=list_proposals
```

![](https://dev.lutece.paris.fr/plugins/plugin-participatoryideation/images/search_page.png)

![](https://dev.lutece.paris.fr/plugins/plugin-participatoryideation/images/submission_sheet.png)

 **Submit form** 

To submit a new proposal, you have to verify that dates of 'ideation' phase are right.

Then access to the following page :

```
http://test.paris.mdp:8080/pb/jsp/site/Portal.jsp?page=ideation&init=true&campaign=<campaign_code>
```

where<campaign_code>is the code of the campaign (for example : A).

![](https://dev.lutece.paris.fr/plugins/plugin-participatoryideation/images/ideation_form.png)

 **Authentication MyLutece** 

Submitting a proposal is subject to authentication via [MyLutece](https://github.com/lutece-platform/lutece-auth-plugin-mylutece) .

## Configuration

To use the plugin into your Lutece site, first add the following code to your pom.xml

```
<dependency>
	<groupId>fr.paris.lutece.plugins</groupId>
	<artifactId>plugin-participatoryideation</artifactId>
	<version>[1.0.0-SNAPSHOT,)</version>
	<type>lutece-plugin</type>
</dependency>
```

Then override the participatory-ideation properties file to configure ideation campaigns :

```
# Code and label of the campaign
participatoryideation.campaign.1=A;First ideation campaign

# Code, label and color of campaign themes
participatoryideation.campaign.A.theme.1=GENERAL;General;#C00000
participatoryideation.campaign.A.theme.2=NUM;Digital city;#BA05E6
participatoryideation.campaign.A.theme.3=...

# Label and type of area (type : 'whole' for whole city, or 'localized' for district ; only one whole area possible)
participatoryideation.campaign.A.area.1=whole city;whole
participatoryideation.campaign.A.area.2=area 1;localized
participatoryideation.campaign.A.area.3=...

# Begin and end of the ideation phase
participatoryideation.campaign.A.ideation=2020-04-05T12:00:00;2020-04-21T11:00:00 

# Optional fields : 4 fields are currently handled, the third can have a complement question

participatoryideation.campaign.A.field1.active=0
participatoryideation.campaign.A.field1.label=Field 1
participatoryideation.campaign.A.field1.description=Description of field 1
participatoryideation.campaign.A.field1.mandatory=0

participatoryideation.campaign.A.field2.active=1
...
```

 **Java classes** 

Campaign data (campaign codes, phase dates, submitter types, themes...) are provided by Java class `IdeationCampaignDataProvider` , which implements the interface `IIdeationCampaignDataProvider` . It can be overrided to provide data from another system. You can view an example in [module-participatoryideation-participatorybudget](https://github.com/lutece-secteur-public/particip-module-participatoryideation-participatorybudget/blob/develop/src/java/fr/paris/lutece/plugins/participatoryideation/modules/participatorybudget/service/ideation/ParticipatoryIdeationCampaignModuleDataProvider.java) . The new data provider class has to be declared by overriding Spring context `participatoryideation_context.xml` .

 **SQL file** 

SQL init file of plugin creates sample data :

 
* A basic campaign with somes phases, themes and areas (see Manage Campaign back-office page)
* Data about submitters (see ManageIdeation back-office page)

To use this SQL file, please refer to the [Lutece Maven process](https://fr.lutece.paris.fr/fr/jsp/site/Portal.jsp?page=wiki&view=page&page_name=maven#H3_Initialize_database) .

# Project Information

 [Issue Tracking](http://dev.lutece.paris.fr/jira/browse/PARTIDEA) : this is a link to the issue management system for this project. Issues (bugs, features, change requests) can be created and queried using this link.


[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/plugin-participatoryideation/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*