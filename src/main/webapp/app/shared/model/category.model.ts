export interface ICategory {
  id?: number;
  title?: string;
  parentCategoryId?: number;
  parentCategory?: Category;
}

export class Category implements ICategory {
  constructor(public id?: number, public title?: string, public parentCategoryId?: number, public parentCategory?: Category) {}
}
