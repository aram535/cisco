/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package com.cisco.main;

import com.cisco.main.services.SidebarPage;
import com.cisco.main.services.SidebarPageConfig;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class SidebarAjaxbasedController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	@Wire
	Grid fnList;
	
	//wire service
	SidebarPageConfig pageConfig = new SidebarPageConfigAjaxBasedImpl();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		
		//to initial view after view constructed.
		Columns columns = fnList.getColumns();
		
		for(SidebarPage page:pageConfig.getPages()){
			Column column = constructSidebarColumn(page.getName(),page.getLabel(),page.getIconUri(),page.getUri());
			columns.appendChild(column);
		}		
	}

	private Column constructSidebarColumn(final String name,String label, String imageSrc, final String locationUri) {
		
		//construct component and hierarchy
		Column column = new Column();
		Image image = new Image(imageSrc);
		Label lab = new Label(label);
		
		//column.appendChild(image);
		column.appendChild(lab);
		
		//set style attribute
		column.setSclass("sidebar-fn");
		//column.setHeight(image.getHeight());
		//new and register listener for events
		EventListener<Event> onActionListener = new SerializableEventListener<Event>(){
			private static final long serialVersionUID = 1L;

			public void onEvent(Event event) throws Exception {
				//redirect current url to new location
				if(locationUri.startsWith("http")){
					//open a new bcolumnser tab
					Executions.getCurrent().sendRedirect(locationUri);
				}else{
					//use iterable to find the first include only
					Include include = (Include) Selectors.iterable(fnList.getPage(), "#mainInclude")
							.iterator().next();
					include.setSrc(locationUri);
					
					//advance bookmark control, 
					//bookmark with a prefix
					if(name!=null){
						getPage().getDesktop().setBookmark("p_"+name);
					}
				}
			}
		};		
		column.addEventListener(Events.ON_CLICK, onActionListener);

		return column;
	}
	
}
