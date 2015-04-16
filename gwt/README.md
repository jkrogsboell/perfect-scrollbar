# `GWT PerfectScrollbar`

## Why GWT PerfectScrollbar?

In GWT you can use ScrollPanel or CustomScrollPanel to add scrollbars to your components. 
The standard ScrollPanel is using native scrolling, which means browser/os look and feel and reduced viewport size, when scrollbars are added.
CustomScrollPanel also uses native scrolling, but allows content to use the entire viewport. 
Also the scrollbars do not show on mobile devices.  

The GWT wrapper for perfect-scrollbar brings you "perfect" scrolling in a GWT like manner and can replace ScrollPanel and CustomScrollPanel if you're brave enough.

## How to use

In your module file use one of the following includes:
```html
<inherits name='com.github.perfectscrollbar.PerfectScrollbar'/> 
```
or
```html
<inherits name='com.github.perfectscrollbar.PerfectScrollbarNoResources'/>
```

PerfectScrollbarPanel implements HasScrolling like ScrollPanel, but allows you to add multiple Panels because it extends FlowPanel rather than SimplePanel.

## Use in UiBinder

```html
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	xmlns:g="urn:import:com.google.gwt.user.client.ui"
	xmlns:scroll="urn:import:com.github.perfectscrollbar.client">
	<g:HTMLPanel>
		<scroll:PerfectScrollbarPanel
			ui:field="scrollPanel" showBars="ALWAYS">
			... add panels here ...				
		</scroll:PerfectScrollbarPanel>
	</g:HTMLPanel>
</ui:UiBinder>
```

## Still to come

Support for optional parameters other than showBars 
     
## Replacement feature 

If you include ScrollPanel and CustomScrollPanel from com.google.gwt.user.client.ui, then all your scrollbars will use the PerfectScrollbarPanel implementation.

Then you can use DataGrid like http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwDataGrid, but with perfect-scrollbar's.
NB: use grid.redraw(), in case of a resize of the grid.  

## Thanks you

To Hyunje Alex Jun for this great plugin.
