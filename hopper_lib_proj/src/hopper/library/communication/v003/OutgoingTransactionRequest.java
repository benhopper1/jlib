package hopper.library.communication.v003;

public class OutgoingTransactionRequest {
	private TransportLayer transportLayer;
	
	public OutgoingTransactionRequest(){
		this.transportLayer = TransportLayer.createTransportLayer();
		this.transportLayer.routingLayer
			.clean()
			.add("type", "transactionToToken")
			.add("stage", "sourceOut")
		;
	}
	
	public RoutingLayer routingLayer(){
		return this.transportLayer.routingLayer;
	}
	
	public DataLayer dataLayer(){
		this.transportLayer.dataLayer.clean();
		return this.transportLayer.dataLayer;
	}
	
	public void dataLayer(DataLayer inDataLayer){
		this.transportLayer.dataLayer = inDataLayer;
	}
	
	public void setTargetTokenId(String inTargetTokenId){
		this.transportLayer.routingLayer.add("targetTokenId", inTargetTokenId);
	}
	
	public TransportLayer toTransportLayer(){
		return transportLayer;
	}
	
	public void setCommand(String inCommand){
		this.transportLayer.routingLayer
			.add("command", inCommand)
		;
	}
}
