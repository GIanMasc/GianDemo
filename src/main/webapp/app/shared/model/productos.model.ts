export interface IProductos {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  precio?: number | null;
  cantidad?: number | null;
}

export const defaultValue: Readonly<IProductos> = {};
