<form class="form" onsubmit="return validateForm(this);" action="jsp/site/Portal.jsp" method="post">
	
	<input type="hidden" name="page" value="ideation">
	<input type="hidden" name="action" value="location">
	
	<#-- *********************************************************************************** -->
	<#-- * THEME THEME THEME THEME THEME THEME THEME THEME THEME THEME THEME THEME THEME T * -->
	<#-- * THEME THEME THEME THEME THEME THEME THEME THEME THEME THEME THEME THEME THEME T * -->
	<#-- *********************************************************************************** -->
	
	<div class="form-group">
		<p>#i18n{participatoryideation.submit_project.steps.location.code_theme_label}</p>
		<div class="row">
			
			<#assign nIndex=0>
			<#assign nMaxIndex=themes?size - 1>
			<#list themes?sort_by("code") as theme>
				<#if nIndex%4==0 >
					<div class="col-xs-6 col-sm-4">
				</#if>
				<div class="radio">
					<label for="code_theme_${theme.code}">
						<input type="radio" id="code_theme_${theme.code}" name="code_theme" value="${theme.code}" <#if form_etape_location.codeTheme??&&theme.code == form_etape_location.codeTheme>checked="checked"</#if>>
						${theme["name"]}
					</label>
				</div>
				<#if nIndex%4==3 || nIndex==nMaxIndex>
					</div>
				</#if>
				<#assign nIndex=nIndex+1>
			</#list>
		
		</div>
	</div>

	<#-- *********************************************************************************** -->
	<#-- * SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER DEPOSIT * -->
	<#-- * SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER DEPOSIT * -->
	<#-- *********************************************************************************** -->
	
	<div id="submitter_row" class="form-group">
		<p>#i18n{participatoryideation.submit_project.steps.location.submitter_label}</p>
		
		<#-- Radio buttons -->
		<#list submitter_types?sort_by("id") as submitter_type>
			<label class="radio-inline">
				<input class="submitter" type="radio" name="submitter_type" id="radio_submitter_${submitter_type["code"]}" value="${submitter_type["code"]}" <#if form_etape_location.submitterType??&&submitter_type["code"] == form_etape_location.submitterType>checked="checked"</#if>>
				${submitter_type["libelle"]}
			</label>
		</#list>

		<#-- Complement fields -->
		<#list submitter_types as submitter_type>
    		<div id="complement_${submitter_type["code"]}" class="submitter-complement">
    		
      			<#if submitter_type["codeComplementType"] != "NONE">
          			<label for="complement_submitter_${submitter_type["code"]}">${submitter_type["libelle"]} :</label>
        		</#if>
        	
        		<#if submitter_type["codeComplementType"] == "LIST">
          			<select id="complement_submitter_${submitter_type["code"]}" name="submitter" class="form-control">
          				<#list submitter_type["values"] as value>
            				<option value="${value["code"]}"<#if form_etape_location.submitterType?? && form_etape_location.submitterType=submitter_type["code"]&&form_etape_location.submitter??&&value.code == form_etape_location.submitter> selected="selected"</#if>>${value["name"]}</option>
          				</#list>
          			</select>
        	
        		<#elseif submitter_type["codeComplementType"] == "FREE">
          			<input type="text" class="form-control" name="submitter" <#if form_etape_location.submitterType?? && form_etape_location.submitterType=submitter_type["code"]>value="${form_etape_location["submitter"]!''}"</#if> id="complement_submitter_${submitter_type["code"]}">
        		</#if>
    		</div>
  		</#list>
  	
  	</div>
  
	<#-- *********************************************************************************** -->
	<#-- * LOCATION LOCATION LOCATION LOCATION LOCATION LOCATION LOCATION LOCATION LOCATIO * -->
	<#-- * LOCATION LOCATION LOCATION LOCATION LOCATION LOCATION LOCATION LOCATION LOCATIO * -->
	<#-- *********************************************************************************** -->
	
  	<div class="form-group">
		<p>#i18n{participatoryideation.submit_project.steps.location.location_type_label}</p>
	    <#if whole_area != "" >
		    <label class="radio-inline">
	    	  	<input type="radio" name="location_type" id="mode_radio_paris" value="whole" <#if form_etape_location.locationType??&& form_etape_location.locationType=='whole'>checked</#if>>
				${whole_area.name}
		    </label>
	    </#if>
	    <#if number_localized_areas != 0 >
	    	<label class="radio-inline">
	        	<input type="radio" name="location_type" id="mode_radio_ardt" value="ardt" <#if form_etape_location.locationType??&& form_etape_location.locationType=='localized'>checked</#if>>
				#i18n{participatoryideation.submit_project.steps.location.location_label_area}
	    	</label>
	    	<div id="col_ardt">
		        <label class="sr-only"></label>
	        	<@comboWithParams name="location_ardt" items=localized_areas default_value="${form_etape_location.locationArdt!''}"additionalParameters=" class=\"form-control input-sm\" " />
	    	</div>
	    </#if>
	</div>

  	<div id="col_geoloc" class="form-group">
		<p>#i18n{participatoryideation.submit_project.steps.location.address_label}</p>
    	
    	<div class="alert alert-warning">FIXME : Need to have a generic geoloc mecanism</div>

    	<div class="input-group">
      		<input type="text" class="form-control input-sm" name="adress" id="adress" value="${form_etape_location.adress!''}">
      		<span class="input-group-btn">
        		<button class="btn btn-default btn-xs" type="button" title="effacer" id="button_delete_adress"><i class="fa fa-times"></i></button>
      		</span>
    	</div>
    	
    	<input id="geojson" type="hidden" name="geojson" value="${(form_etape_location.geojson?html)!''}">
    	<input id="geojson_state" type="hidden">
		<br/>
		<div class="form-control-static" id="map">&nbsp;</div>
  	</div>
  
  	<div class="form-button text-center">
    	<button type="submit" value="Continuer" class="btn btn-14rem btn-black-on-white">#i18n{participatoryideation.submit_project.steps.location.submit} <i class="fa fa-angle-down"></i></button>
  	</div>

	</form>

<script type="text/javascript" src="js/proj4js-combined.js"></script>
<script type="text/javascript" src="jsp/site/plugins/address/modules/autocomplete/SetupSuggestPOI.js.jsp"></script>
<script type="text/javascript" src="js/plugins/address/modules/autocomplete/jQuery.suggestPOI.js"></script>
<script type="text/javascript" src="js/plugins/leaflet/leaflet/leaflet.js"></script>
<script type="text/javascript" src="js/plugins/leaflet/esri/esri-leaflet.js"></script>
<script type='text/javascript'>

// Ajout
$( function(){
	$(".submitter-complement").toggle();
  
	$('[id ^= code_theme_]').on("click", function(e){
		if ($('#code_theme_CADRE').prop('checked')) {
			$('#msg_theme_CADRE').modal();
		};
	});
  
	$('#mode_radio_paris').on("click", function(e){
		$('#col_ardt').hide();
	});
	$('#mode_radio_ardt').on("click", function(e){
		$('#col_ardt').show();
	});
	if ($('#mode_radio_ardt').is(":checked")){
		$('#col_ardt').show();
	} else{
		$('#col_ardt').hide();
	}

	$(".submitter").on("click", function(e){
		clickRadioSubmitter( this );
	});

	var checked_elem = $('#submitter_row').find("input[type=radio]:checked")[0];
	if ( typeof(checked_elem) != 'undefined' ) {
		clickRadioSubmitter( checked_elem );
	}
	
});
// Fin ajout

function clickRadioSubmitter( elem ) {
  $('.submitter-complement').hide();
  $('#complement_' + elem.value ).toggle();
  $('.submitter-complement').find('input').removeAttr('name');
  $('.submitter-complement').find('select').removeAttr('name');
  $('#complement_submitter_' + elem.value ).attr('name', 'submitter');
}

var loadresource = document.createElement('link');
loadresource.setAttribute("rel", "stylesheet");
loadresource.setAttribute("type", "text/css");
loadresource.setAttribute("href", "js/plugins/leaflet/leaflet/leaflet.css");
document.getElementsByTagName("head")[0].appendChild(loadresource);
loadresource = document.createElement('link');
loadresource.setAttribute("rel", "stylesheet");
loadresource.setAttribute("type", "text/css");
loadresource.setAttribute("href", "js/jquery/plugins/ui/css/jquery-ui.css");
document.getElementsByTagName("head")[0].appendChild(loadresource);

$(window).load(function() {
  var map = L.map('map').setView([48.85632, 2.33272], 12);
  var markers_layer = L.featureGroup().addTo(map);
  var esri_streets = L.esri.basemapLayer('Streets').addTo(map);

	// Ajout du perimetre des quartiers populaires
	var layergroup_qpvqva = L.layerGroup();
	var arrondissements_layer_qpv  = L.esri.featureLayer( { "url": 'https://services1.arcgis.com/yFAX7hJID4ONeUHP/arcgis/rest/services/QPV_QVA_GPRU/FeatureServer/0', "style": { "color": "#ffffff", "weight": 0, "opacity": 1, "fillOpacity": 0.3, "fillColor": "#33ff33", }, "where": "C_DEP='75' AND C_NAT_QPV='NQPV'" });
	var arrondissements_layer_qva  = L.esri.featureLayer( { "url": 'https://services1.arcgis.com/yFAX7hJID4ONeUHP/arcgis/rest/services/QPV_QVA_GPRU/FeatureServer/0', "style": { "color": "#ffffff", "weight": 0, "opacity": 1, "fillOpacity": 0.3, "fillColor": "#33ff33", }, "where": "C_DEP='75' AND C_NAT_QPV='QVA'"  });
	var arrondissements_layer_gpru = L.esri.featureLayer( { "url": 'https://services1.arcgis.com/yFAX7hJID4ONeUHP/arcgis/rest/services/QPV_QVA_GPRU/FeatureServer/0', "style": { "color": "#ffffff", "weight": 0, "opacity": 1, "fillOpacity": 0.3, "fillColor": "#33ff33", }, "where": "GPRU_NOM!=''"                    });
	var arrondissements_layer_qbp  = L.esri.featureLayer( { "url": 'https://services1.arcgis.com/yFAX7hJID4ONeUHP/arcgis/rest/services/QPV_QVA_GPRU/FeatureServer/0', "style": { "color": "#ffffff", "weight": 0, "opacity": 1, "fillOpacity": 0.3, "fillColor": "#33ff33", }, "where": "EXT_BP!=''"                      });
	layergroup_qpvqva.addLayer(arrondissements_layer_qpv);
	layergroup_qpvqva.addLayer(arrondissements_layer_qva);
	layergroup_qpvqva.addLayer(arrondissements_layer_gpru);
	layergroup_qpvqva.addLayer(arrondissements_layer_qbp);
	map.addLayer(layergroup_qpvqva);

  if (!Proj4js.defs["EPSG:27561"]){
    Proj4js.defs["EPSG:27561"]="+title=NTF (Paris) / Lambert Nord France +proj=lcc +lat_1=49.50000000000001 +lat_0=49.50000000000001 +lon_0=0 +k_0=0.999877341 +x_0=600000 +y_0=200000 +a=6378249.2 +b=6356515 +towgs84=-168,-60,320,0,0,0,0 +pm=paris +units=m +no_defs ";
  }
  var sourceSRID = new Proj4js.Proj('EPSG:27561');
  var destSRID = new Proj4js.Proj('EPSG:4326');
  
   var jAdresse = $('#adress');
   /* jAdresse.suggestPOI();
   jAdresse.bind($.suggestPOI.EVT_SELECT, function(event) {
                   var poi = event.poi;
                   if (poi) {
                       var address = poi.libelleTypo;
                       var lambert_x = poi.x;
                       var lambert_y = poi.y;
                       p = new Proj4js.Point(lambert_x, lambert_y);
                       Proj4js.transform(sourceSRID, destSRID, p);

                       markers_layer.clearLayers();
                       var marker = L.marker([p.y, p.x]).addTo(markers_layer);
                       map.fitBounds(markers_layer.getBounds());
                       var obj= {
                           "type": "Feature",
                           "properties": {
                               "address": address
                           },
                           "geometry": {
                               "type": "Point",
                               "coordinates": [p.x, p.y]
                           }
                       };
                       $('#geojson').val(JSON.stringify(obj));
                       $('#geojson_state').val(JSON.stringify(obj));
                       }
               });
 
               $("#button_delete_adress").on('click', function () {
                       $('#adress').val("");
                       //Don't put the empty string to disambiguate with the user
                       //clearing the field, and then pressing the browser back button
                       $('#geojson_state').val("false");
                       $('#geojson').val("");
                       map.setView([48.85632, 2.33272], 12);
                       markers_layer.clearLayers();
               });

               //Try to restore from back/forward browser history buttons
               var prev_data = $('#geojson_state').val();
               var prev_value;
               var user_cleared = false;
               if (prev_data) {
                   prev_value=JSON.parse(prev_data);
                   if (prev_value) {
                       $('#geojson').val(JSON.stringify(prev_value));
                   } else {
         user_cleared = true;
       }
   } else {
  var geojson_val = $('#geojson').val();
  if (geojson_val) {
     prev_value=JSON.parse(geojson_val);
  } else {
     user_cleared = true;
  }
 }
 if (!user_cleared) {
   $('#adress').val(prev_value.properties.address);
   markers_layer.clearLayers();
   var marker = L.marker([prev_value.geometry.coordinates[1],prev_value.geometry.coordinates[0]]).addTo(markers_layer);
   map.fitBounds(markers_layer.getBounds());
   } */
});
</script>
