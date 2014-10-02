package com.compuware.apm.ruxit.synth.analyzer.output;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class EventSourceSupport implements EventSource {

	private Map<AnalyzerEvent.Type, Set<EventListener>> listenersByType = new ConcurrentHashMap<>();
	private Map<EventListener, Set<AnalyzerEvent.Type>> typesByListener = new ConcurrentHashMap<>();

	public EventSourceSupport() {}
	
	@Override
	public void addEventListener (EventListener listener, AnalyzerEvent.Type ... types) {
		if (listener == null || types == null) {
			throw new NullPointerException();
		}
	    Set<AnalyzerEvent.Type> listenerTypes = typesByListener.get(listener);
        if (listenerTypes == null) {
        	listenerTypes = new CopyOnWriteArraySet<>();
        	typesByListener.put(listener, listenerTypes);
        }
		for (AnalyzerEvent.Type type : types) {
			listenerTypes.add(type);
			Set<EventListener> listeners = listenersByType.get(type);
			if (listeners == null) {
				listeners = new CopyOnWriteArraySet<>();
				listenersByType.put(type, listeners);
			}
			listeners.add(listener);
		}
	}
	
	@Override
	public void addEventListener (EventListener listener) {
       addEventListener(listener, AnalyzerEvent.Type.values());
	}
	
	@Override
	public void removeEventListener (EventListener listener, AnalyzerEvent.Type ... types) {
		if (listener == null || types == null) {
			throw new NullPointerException();
		}
	    Set<AnalyzerEvent.Type> listenerTypes = typesByListener.get(listener);
		for (AnalyzerEvent.Type type : types) {
			if (listenerTypes != null) {
			    listenerTypes.remove(type);
			}
			Set<EventListener> listeners = listenersByType.get(type);
			if (listeners != null) {
				listeners.remove(type);
			}
		}
		if (listenerTypes.isEmpty()) {
			typesByListener.remove(listener);
		}
	}
	
	@Override
	public void removeEventListener(EventListener listener) {
	    Set<AnalyzerEvent.Type> listenerTypes = typesByListener.get(listener);
        if (listenerTypes == null) {
        	return;
        }
	    removeEventListener(listener, listenerTypes.toArray(new AnalyzerEvent.Type [listenerTypes.size()]));		
	}
	
	public void notify (AnalyzerEvent event) {
		Set<EventListener> listeners = listenersByType.get(event.getType());
		if (listeners == null) {
			return;
		}
		for (EventListener listener : listeners) {
			listener.onEvent(event);
		}
	}

	@Override
	public String toString() {
		return "EventSourceSupport [listenersByType=" + listenersByType
				+ ", typesByListener=" + typesByListener + "]";
	}
}
