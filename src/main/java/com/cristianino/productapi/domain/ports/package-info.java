/**
 * Domain ports package containing the interfaces that define the boundaries of the application.
 * 
 * In Hexagonal Architecture (Ports and Adapters), ports are interfaces that define the contract
 * between the core business logic and the external world.
 * 
 * There are two types of ports:
 * 
 * 1. Inbound Ports (Primary Ports): Define what the application can do
 *    - CreateProductUseCase: Defines the interface for creating products
 * 
 * 2. Outbound Ports (Secondary Ports): Define what the application needs from external systems
 *    - LoadProductPort: Defines the interface for loading products from storage
 *    - SaveProductPort: Defines the interface for saving products to storage
 * 
 * These ports are implemented by:
 * - Inbound ports: Application services in the application layer
 * - Outbound ports: Adapters in the infrastructure layer
 * 
 * @author cristianino
 * @version 1.0.0
 */
package com.cristianino.productapi.domain.ports;