// PC3 — MongoDB: validaciones, índices, datos semilla
// Ejecutar: mongosh "mongodb://tcya_mongo:tcya_mongo_local@localhost:27017/todocambioya_docs" init-collections.js

db = db.getSiblingDB('todocambioya_docs');

db.createCollection('notificaciones_push', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['usuarioId', 'tipo', 'titulo', 'creadoEn'],
      properties: {
        usuarioId: { bsonType: 'string', description: 'UUID del usuario en PostgreSQL' },
        tipo: { enum: ['orden', 'alerta', 'promo', 'chat'] },
        titulo: { bsonType: 'string', minLength: 3, maxLength: 200 },
        contenido: { bsonType: 'string' },
        regionCodigo: { enum: ['LIM', 'AQP', 'TRU', 'CUS', 'PIU'] },
        leida: { bsonType: 'bool' },
        metadata: { bsonType: 'object' },
        creadoEn: { bsonType: 'date' }
      }
    }
  },
  validationLevel: 'strict',
  validationAction: 'error'
});

db.notificaciones_push.createIndex({ usuarioId: 1, leida: 1 });
db.notificaciones_push.createIndex({ creadoEn: -1 });
db.notificaciones_push.createIndex({ regionCodigo: 1, tipo: 1 });

db.notificaciones_push.insertMany([
  {
    usuarioId: 'carlos-uuid-demo',
    tipo: 'orden',
    titulo: 'Orden completada',
    contenido: 'Tu cambio USD 1000 fue procesado',
    regionCodigo: 'LIM',
    leida: false,
    metadata: { ordenNumero: 'ORD-LIM-0001', canal: 'push' },
    creadoEn: new Date()
  },
  {
    usuarioId: 'ana-uuid-demo',
    tipo: 'alerta',
    titulo: 'Tasa objetivo alcanzada',
    contenido: 'USD/PEN llegó a 3.75',
    regionCodigo: 'LIM',
    leida: true,
    metadata: { tasaObjetivo: 3.75 },
    creadoEn: new Date()
  },
  {
    usuarioId: 'maria-uuid-demo',
    tipo: 'promo',
    titulo: 'Cupón BIENVENIDA10',
    contenido: '10% de descuento en tu próximo cambio',
    regionCodigo: 'AQP',
    leida: false,
    metadata: { cuponCodigo: 'BIENVENIDA10' },
    creadoEn: new Date()
  },
  {
    usuarioId: 'luis-uuid-demo',
    tipo: 'chat',
    titulo: 'Soporte en línea',
    contenido: 'Hola, ¿en qué puedo ayudarte?',
    regionCodigo: 'TRU',
    leida: false,
    metadata: { agente: 'Soporte-01', mensajes: 3 },
    creadoEn: new Date()
  }
]);

print('=== PROYECCIÓN (solo titulo, tipo, leida) ===');
printjson(db.notificaciones_push.find(
  { leida: false },
  { _id: 0, titulo: 1, tipo: 1, leida: 1, creadoEn: 1 }
).toArray());

print('=== AGREGACIÓN: conteo por tipo y región ===');
printjson(db.notificaciones_push.aggregate([
  { $group: { _id: { tipo: '$tipo', region: '$regionCodigo' }, total: { $sum: 1 } } },
  { $sort: { total: -1 } }
]).toArray());

print('=== AGREGACIÓN: notificaciones no leídas por usuario ===');
printjson(db.notificaciones_push.aggregate([
  { $match: { leida: false } },
  { $group: { _id: '$usuarioId', pendientes: { $sum: 1 } } }
]).toArray());

print('MongoDB PC3 init completado.');
